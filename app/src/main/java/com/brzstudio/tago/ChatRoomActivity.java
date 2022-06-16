package com.brzstudio.tago;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatRoomActivity extends AppCompatActivity {

    public class ListItem {
        private String listUid;
        private String listMessage;

        public String getListUid() {
            return listUid;
        }
        public void setListUid(String s) {
            this.listUid = s;
        }
        public String getListMessage() {
            return listMessage;
        }
        public void setListMessage(String s) {
            this.listMessage = s;
        }


        ListItem(String uid, String message) {
            this.listUid = uid;
            this.listMessage = message;

        }
    }
    private class ListItemAdapter extends BaseAdapter {
        ArrayList<ListItem> items = new ArrayList<ListItem>();
        Context context;

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            context = parent.getContext();
            ListItem listItem = items.get(position);

            if (convertView == null) {
                if (listItem.getListUid().equals(LoginedUserData.getUid())) {
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(R.layout.listview_chatroom_my_msg, parent, false);

                    TextView my_msg = convertView.findViewById(R.id.my_msg);
                    my_msg.setText(listItem.getListMessage());

                    return convertView;

                } else if (!listItem.getListUid().equals(LoginedUserData.getUid())) {
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(R.layout.listview_chatroom_passenger_msg, parent, false);

                    TextView passenger_name = convertView.findViewById(R.id.passenger_name);
                    TextView passenger_msg = convertView.findViewById(R.id.passenger_msg);
                    passenger_name.setText(UidNicknameData.getUidNicknameMap().get(listItem.getListUid()));
                    passenger_msg.setText(listItem.getListMessage());

                    return convertView;
                }
            }

            return convertView;
        }

        public void addItem(ListItem item) {
            items.add(item);
        }
    }

    LinearLayout topBar;
    FirebaseFirestore firestore;

    ListView listView;
    ListItemAdapter adapter;

    EditText messageEditText;

    ImageButton sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        setResult(99);



        firestore = FirebaseFirestore.getInstance();
        adapter = new ListItemAdapter();

//        // topBar에서 상단바만큼 margin 추가
//        topBar = findViewById(R.id.topBar);
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
//        params.setMargins(0, getStatusBarHeight(this), 0, 0);
//        topBar.setLayoutParams(params);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        // 리스트뷰 셋업
        listView = findViewById(R.id.list);

        //파이어베이스
        // 이전 intent 데이터
        Intent intendData = getIntent();
        String uid = intendData.getStringExtra("uid");
        // 데이터 가져오기
        firestore.collection("TagoParty").document(uid).collection("messages")
                .orderBy("date", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        System.out.println("uid is : " + uid);
                        if (error != null) {
                            System.out.println("Listen failed : " + error);
                            return;
                        }

                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                System.out.println("ADDED" + dc.getDocument().getData().get("asdfasdf"));
                                adapter.addItem(new ListItem((String) dc.getDocument().getData().get("sender_uid"), (String) dc.getDocument().getData().get("message")));
                                listView.setAdapter(adapter);
                            } else if (dc.getType() == DocumentChange.Type.MODIFIED) {
                                System.out.println("MODIFIED : " + dc.getDocument().getData().get("asdfasdf"));
                            } else if (dc.getType() == DocumentChange.Type.REMOVED) {
                                System.out.println("REMOVED");
                            }
                        }
                    }
                });



        sendButton = findViewById(R.id.sendButton);
        messageEditText = findViewById(R.id.messageEditText);
        sendButton.setOnClickListener(view -> {
            String text = messageEditText.getText().toString();
            DocumentReference documentReference = firestore.collection("TagoParty").document(uid).collection("messages").document();
            Map<String, Object> chatInfo = new HashMap<>();
            chatInfo.put("date", new Timestamp(new Date()));
            chatInfo.put("message", text);
            chatInfo.put("sender_uid", LoginedUserData.getUid());
            documentReference.set(chatInfo);
            messageEditText.setText("");

        });



    }

    protected void onStart() {
        super.onStart();


    }

    // 상단바 높이 구하는 함수
    public static int getStatusBarHeight(Context context) {
        int screenSizeType = (context.getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK);

        int statusbar = 0;
        if (screenSizeType != Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");

            if (resourceId > 0) {
                statusbar = context.getResources().getDimensionPixelSize(resourceId);
            }
        }

        return statusbar;
    }
}