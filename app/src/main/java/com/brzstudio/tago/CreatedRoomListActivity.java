package com.brzstudio.tago;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreatedRoomListActivity extends AppCompatActivity {

    public class ListItem {
        private String listAuthor;
        private String listDeparture;
        private String listArrival;
        private String listMeter;
        private int listGender;
        private String listNowmax;

        public String getListAuthor() {
            return listAuthor;
        }
        public void setListAuthor(String s) {
            this.listAuthor = s;
        }
        public String getListDeparture() {
            return listDeparture;
        }
        public void setListDeparture(String s) {
            this.listDeparture = s;
        }
        public String getListArrival() {
            return listArrival;
        }
        public void setListArrival(String s) {
            this.listArrival = s;
        }
        public String getListMeter() {
            return listMeter;
        }
        public void setListMeter(String s) {
            this.listMeter = s;
        }
        public int getListGender() {
            return listGender;
        }
        public void setListGender(int s) {
            this.listGender = s;
        }
        public String getListNowmax() {
            return listNowmax;
        }
        public void setListNowmax(String s) {
            this.listNowmax = s;
        }


        ListItem(String author, String departure, String arrival, String meter, int gender, String nowmax) {
            this.listAuthor = author;
            this.listDeparture = departure;
            this.listArrival = arrival;
            this.listMeter = meter;
            this.listGender = gender;
            this.listNowmax = nowmax;
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
                if (listItem.getListGender() == 0) {
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(R.layout.listview_created_room_gender_0, parent, false);
                } else if (listItem.getListGender() == 1) {
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(R.layout.listview_created_room_gender_1, parent, false);
                } else if (listItem.getListGender() == 2) {
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(R.layout.listview_created_room_gender_2, parent, false);
                }

            }

            TextView authorText = convertView.findViewById(R.id.authorText);
            TextView departureText = convertView.findViewById(R.id.departureText);
            TextView arrivalText = convertView.findViewById(R.id.arrivalText);
            TextView meter = convertView.findViewById(R.id.meter);
            TextView nowmax = convertView.findViewById(R.id.nowmax);

            authorText.setText(listItem.getListAuthor());
            departureText.setText(listItem.getListDeparture());
            arrivalText.setText(listItem.getListArrival());
            meter.setText(listItem.getListMeter());
            nowmax.setText(listItem.getListNowmax());


            return convertView;
        }

        public void addItem(ListItem item) {
            items.add(item);
        }
    }

    LinearLayout topBar;
    ListView listView;
    ListItemAdapter adapter;
    TextView numberOfPeople;
    ArrayList<Map> resultList;

    Button createNewRoomButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_created_room_list);

        // topBar에서 상단바만큼 margin 추가
        topBar = findViewById(R.id.topBar);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        params.setMargins(0, getStatusBarHeight(this), 0, 0);
        topBar.setLayoutParams(params);

        // 버튼
        createNewRoomButton = findViewById(R.id.createNewRoomButton);
        createNewRoomButton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), CreateNewRoomActivity.class);
            startActivity(intent);
        });

        resultList = (ArrayList<Map>) getIntent().getSerializableExtra("result");

        // 인원 수
        int numberOfPeopleData = resultList.size();
        numberOfPeople = findViewById(R.id.numberOfPeople);
        numberOfPeople.setText(String.valueOf(numberOfPeopleData));

        // 리스트뷰 셋업
        listView = findViewById(R.id.list);
        adapter = new ListItemAdapter();


        for(int i = 0; i < resultList.size(); i++) {
            String author = UidNicknameData.getUidNicknameMap().get((String) resultList.get(i).get("author_uid"));
            System.out.println("author test" + author);
            String departure = (String) resultList.get(i).get("departure");
            String arrival = (String) resultList.get(i).get("arrival");
            int gender = (int) (long) resultList.get(i).get("gender");
            int max_people = (int) (long) resultList.get(i).get("max_people");
            List<String> joined_uid = (List<String>) resultList.get(i).get("joined_uid");
            String nowmax = joined_uid.size() + "/" + max_people;

            adapter.addItem(new ListItem(author, departure, arrival, "124", gender, nowmax));
        }

        listView.setAdapter(adapter);

        // 리스트뷰 클릭 이벤트
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String departure = (String) resultList.get(position).get("departure");
                String arrival = (String) resultList.get(position).get("arrival");
                String departureAddress = (String) resultList.get(position).get("departure_address");
                String arrivalAddress = (String) resultList.get(position).get("arrival_address");
                int max_people = (int) (long) resultList.get(position).get("max_people");
                List<String> joined_uid = (List<String>) resultList.get(position).get("joined_uid");
                String nowmax = joined_uid.size() + "/" + max_people;
                String departureX = resultList.get(position).get("departureX") + "";
                String departureY = resultList.get(position).get("departureY") + "";

                Intent intent = new Intent(getApplicationContext(), RoomInformationActivity.class);
                intent.putExtra("departure", departure);
                intent.putExtra("arrival", arrival);
                intent.putExtra("departureAddress", departureAddress);
                intent.putExtra("arrivalAddress", arrivalAddress);
                intent.putExtra("nowmax", nowmax);
                intent.putExtra("departureX", departureX);
                intent.putExtra("departureY", departureY);

                startActivity(intent);


            }
        });

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