package com.brzstudio.tago;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreatedRoomListActivity extends AppCompatActivity implements LocationListener {

    // 경도 위도 미터 단위 거리 계산
    private static double distance(double lat1, double lon1, double lat2, double lon2) {

        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));

        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 1609.344;

        return (dist);
    }

    // This function converts decimal degrees to radians
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    // This function converts radians to decimal degrees
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

    public Location getCurrentLocation(Context mContext) {
        int MIN_TIME_BW_UPDATES = 1000;
        int MIN_DISTANCE_CHANGE_FOR_UPDATES = 5;
        Location loc = null;
        Double latitude, longitude;

        LocationManager locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

        // getting GPS status
        Boolean checkGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        // getting network status
        Boolean checkNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!checkGPS && !checkNetwork) {
            Toast.makeText(mContext, "No Service Provider Available", Toast.LENGTH_SHORT).show();
        } else {
            //this.canGetLocation = true;
            // First get location from Network Provider
            if (checkNetwork) {
                try {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    if (locationManager != null) {
                        loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    }
                    if (loc != null) {
                        Log.d(TAG, "getCurrentLocation: " + loc.getLatitude() + ", " + loc.getLongitude());
                        return loc;
                    }
                } catch (SecurityException e) {
                }
            }
        }
        // if GPS Enabled get lat/long using GPS Services
        if (checkGPS) {
            if (loc == null) {
                try {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d("GPS Enabled", "GPS Enabled");
                    if (locationManager != null) {
                        loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (loc != null) {
                            latitude = loc.getLatitude();
                            longitude = loc.getLongitude();
                        }
                    }
                } catch (SecurityException e) {
                }
            }
        }
        Location locErr = null;
        return locErr;
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

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

            Location loc = getCurrentLocation(getApplicationContext());
            int dis = (int) distance(loc.getLatitude(), loc.getLongitude(), (double) resultList.get(i).get("departureX") , (double) resultList.get(i).get("departureY"));

            adapter.addItem(new ListItem(author, departure, arrival, dis + "m", gender, nowmax));
        }

        listView.setAdapter(adapter);

        // 리스트뷰 클릭 이벤트
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String uid = (String) resultList.get(position).get("uid");
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
                intent.putExtra("uid", uid);
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