package com.brzstudio.tago;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

public class CreatedRoomListActivity extends AppCompatActivity {

    public class ListItem {
        private String listAuthor;
        private String listDeparture;
        private String listArrival;
        private String listMeter;
        private String listGender;
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
        public String getListGender() {
            return listGender;
        }
        public void setListGender(String s) {
            this.listGender = s;
        }
        public String getListNowmax() {
            return listNowmax;
        }
        public void setListNowmax(String s) {
            this.listNowmax = s;
        }


        ListItem(String author, String departure, String arrival, String meter, String gender, String nowmax) {
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
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.listview_created_room, parent, false);
            }

            TextView authorText = convertView.findViewById(R.id.authorText);
            TextView departureText = convertView.findViewById(R.id.departureText);
            TextView arrivalText = convertView.findViewById(R.id.arrivalText);
            TextView meter = convertView.findViewById(R.id.meter);
            TextView gender = convertView.findViewById(R.id.gender);
            TextView nowmax = convertView.findViewById(R.id.nowmax);

            authorText.setText(listItem.getListAuthor());
            departureText.setText(listItem.getListDeparture());
            arrivalText.setText(listItem.getListArrival());
            meter.setText(listItem.getListMeter());
            gender.setText(listItem.getListGender());
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_created_room_list);

        // topBar에서 상단바만큼 margin 추가
        topBar = findViewById(R.id.topBar);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        params.setMargins(0, getStatusBarHeight(this), 0, 0);
        topBar.setLayoutParams(params);

        ArrayList<Map> resultList = (ArrayList<Map>) getIntent().getSerializableExtra("result");

        System.out.println(resultList);

        // 리스트뷰 셋업
        listView = findViewById(R.id.list);
        adapter = new ListItemAdapter();

        adapter.addItem(new ListItem("author", "천안", "상명", "124", "남", "1/3"));
        listView.setAdapter(adapter);


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