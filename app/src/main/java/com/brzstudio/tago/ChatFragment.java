package com.brzstudio.tago;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatFragment extends Fragment implements View.OnClickListener {

    public class ChatListItem{
//        private String oldProfilePic;
        private String oldDepartureText;
        private String oldDestinationText;
        private String oldLastChatText;
        private String oldLivePeople;
        private String oldTotalPeople;

//        public String getOldProfilePic() { return oldProfilePic; }
//        public void setOldProfilePic(String oldProfilePic) { this.oldProfilePic = oldProfilePic; }
        public String getOldDepartureText() { return oldDepartureText; }
        public void setOldDepartureText(String oldDepartureText) { this.oldDepartureText = oldDepartureText; }
        public String getOldDestinationText() { return oldDestinationText; }
        public void setOldDestinationText(String oldDestinationText) { this.oldDestinationText = oldDestinationText; }
        public String getOldLastChatText() { return oldLastChatText; }
        public void setOldLastChatText(String oldLastChatText) { this.oldLastChatText = oldLastChatText; }
        public String getOldLivePeople() { return oldLivePeople; }
        public void setOldLivePeople(String oldLivePeople) { this.oldLivePeople = oldLivePeople; }
        public String getOldTotalPeople() { return oldTotalPeople; }
        public void setOldTotalPeople(String oldTotalPeople) { this.oldTotalPeople = oldTotalPeople; }

        ChatListItem(String oldDepartureText, String oldDestinationText, String oldLastChatText, String oldLivePeople, String oldTotalPeople) {
//            this.oldProfilePic = oldProfilePic;
            this.oldDepartureText = oldDepartureText;
            this.oldDestinationText = oldDestinationText;
            this.oldLastChatText = oldLastChatText;
            this.oldLivePeople = oldLivePeople;
            this.oldTotalPeople = oldTotalPeople;
        }
    }
    public class ChatListItemAdapter extends BaseAdapter {
        ArrayList<ChatListItem> items = new ArrayList<ChatListItem>();
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
            ChatListItem chatListItem = items.get(position);

            if(convertView == null) {
                LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.listview_fragment_chat, parent, false);
            }

            TextView oldDepartureTextText = convertView.findViewById(R.id.oldDepartureText);
            TextView oldDestinationTextText = convertView.findViewById(R.id.oldDestinationText);
            TextView oldLastChatTextText = convertView.findViewById(R.id.oldLastChatText);
            TextView oldLivePeopleTextText = convertView.findViewById(R.id.oldLivePeople);
            TextView oldTotalPeopleTextText = convertView.findViewById(R.id.oldTotalPeople);

            oldDepartureTextText.setText(chatListItem.getOldDepartureText());
            oldDestinationTextText.setText(chatListItem.getOldDestinationText());
            oldLastChatTextText.setText(chatListItem.getOldLastChatText());
            oldLivePeopleTextText.setText(chatListItem.getOldLivePeople());
            oldTotalPeopleTextText.setText(chatListItem.getOldTotalPeople());

            return convertView;
        }
        public void addItem(ChatListItem item) {
            items.add(item);
        }
    }

    ListView listView;
    ChatListItemAdapter adapter;
    String lastChat;
    List<String> tempUidList;
    ImageView refreshButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_chat,container,false);
        adapter = new ChatListItemAdapter();

        listView = v.findViewById(R.id.list);

        refreshButton = v.findViewById(R.id.refresh);
        refreshButton.setOnClickListener(this);


        getChatRoom(inIsTaskDone -> {

        });

        // 리스트뷰 클릭 이벤트
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String uid = tempUidList.get(position);

                Intent intent = new Intent(getContext(), ChatRoomActivity.class);
                intent.putExtra("uid", uid);

                startActivity(intent);


            }
        });

        // Inflate the layout for this fragment
        return v;
    }

    //버튼 이벤트 처리
    public void onClick(View view) {
        if(view.getId() == R.id.refresh) {
            getChatRoom(inIsTaskDone -> {
                adapter = new ChatListItemAdapter();
            });
        }
    }

    public void getLastMessage(String uid, final isTaskDoneCallback2 callback) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        CollectionReference partyRef = firestore.collection("TagoParty").document(uid).collection("messages");

        partyRef.orderBy("date", Query.Direction.DESCENDING).limit(1).get().addOnCompleteListener(task -> {
            lastChat = "";
            for (QueryDocumentSnapshot document : task.getResult()) {
                lastChat = (String) document.getData().get("message");
                System.out.println("lastChat is + " + lastChat);
            }
            callback.onCallback2(true);
        });


    }

    public void getChatRoom(final isTaskDoneCallback callback) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        CollectionReference partyRef = firestore.collection("TagoParty");

        partyRef.get().addOnCompleteListener(task -> {

            if (task.isSuccessful()) {
                tempUidList = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    System.out.println("-----------Succ----------");

                    List<String> joined_uid = (List<String>) document.get("joined_uid");

                    assert joined_uid != null;
                    if(joined_uid.contains(LoginedUserData.getUid())) {
                        getLastMessage(document.getId(), inIsTaskDone2 -> {

                            tempUidList.add(document.getId());
                            System.out.println("joined uid size" + joined_uid.size() + lastChat);

                            adapter.addItem(new ChatListItem((String) document.getData().get("departure"),
                                    (String) document.getData().get("arrival"),
                                    lastChat,
                                    joined_uid.size() + "",
                                    document.getData().get("max_people").toString()));
                            listView.setAdapter(adapter);

                        });


                    }
                }
            } else {
                System.out.println("task0 : Error getting documents: " + task.getException());
            }
            callback.onCallback(true);

        });

    }

    public interface isTaskDoneCallback {
        void onCallback(boolean inIsTaskDone);
    }

    public interface isTaskDoneCallback2 {
        void onCallback2(boolean inIsTaskDone2);
    }
}