package com.brzstudio.tago;

import static android.content.ContentValues.TAG;
import static android.content.Context.LOCATION_SERVICE;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.util.FusedLocationSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListFragment extends Fragment implements View.OnClickListener, LocationListener {

    ArrayList<Map<String, Object>> result;

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

    public class NearList {
        private String nearAuthor;
        private String nearDeparture;
        private String nearArrival;
        private String nearMeter;
        private int nearGender;
        private String nearNowmax;

        public String getNearAuthor() {
            return nearAuthor;
        }

        public void setNearAuthor(String nearAuthor) {
            this.nearAuthor = nearAuthor;
        }

        public String getNearDeparture() {
            return nearDeparture;
        }

        public void setNearDeparture(String nearDeparture) {
            this.nearDeparture = nearDeparture;
        }

        public String getNearArrival() {
            return nearArrival;
        }

        public void setNearArrival(String nearArrival) {
            this.nearArrival = nearArrival;
        }

        public String getNearMeter() {
            return nearMeter;
        }

        public void setNearMeter(String nearMeter) {
            this.nearMeter = nearMeter;
        }

        public int getNearGender() {
            return nearGender;
        }

        public void setNearGender(int nearGender) {
            this.nearGender = nearGender;
        }

        public String getNearNowmax() {
            return nearNowmax;
        }

        public void setNearNowmax(String nearNowmax) {
            this.nearNowmax = nearNowmax;
        }

        NearList(String nearAuthor, String nearDeparture, String nearArrival, String nearMeter, int nearGender, String nearNowmax) {
            this.nearAuthor = nearAuthor;
            this.nearDeparture = nearDeparture;
            this.nearArrival = nearArrival;
            this.nearMeter = nearMeter;
            this.nearGender = nearGender;
            this.nearNowmax = nearNowmax;
        }
    }

    public class NearListAdapter extends BaseAdapter {
        ArrayList<NearList> items = new ArrayList<NearList>();
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
            NearList nearList = items.get(position);

            if (convertView == null) {
              if (nearList.getNearGender() == 0) {
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(R.layout.listview_created_room_gender_0, parent, false);
                } else if (nearList.getNearGender() == 1) {
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(R.layout.listview_created_room_gender_1, parent, false);
                } else if (nearList.getNearGender() == 2) {
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(R.layout.listview_created_room_gender_2, parent, false);
                }
            }

            TextView nearAuthorText = convertView.findViewById(R.id.authorText);
            TextView nearDepartureText = convertView.findViewById(R.id.departureText);
            TextView nearArrivalText = convertView.findViewById(R.id.arrivalText);
            TextView meter = convertView.findViewById(R.id.meter);
            TextView nowmax = convertView.findViewById(R.id.nowmax);

            nearAuthorText.setText(nearList.getNearAuthor());
            nearDepartureText.setText(nearList.getNearDeparture());
            nearArrivalText.setText(nearList.getNearArrival());
            meter.setText(nearList.getNearMeter());
            nowmax.setText(nearList.getNearNowmax());

            return convertView;
        }

        public void addItem(NearList item) {
            items.add(item);
        }
    }

    ListView listView;
    NearListAdapter adapter;
    ImageView refreshButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        adapter = new NearListAdapter();

        listView = view.findViewById(R.id.list);

        refreshButton = view.findViewById(R.id.refresh);
        refreshButton.setOnClickListener(this);


        Location loc = getCurrentLocation(getContext());

        getRoomList(inIsTaskDone -> {
            for (int i = 0; i < result.size(); i++) {
                int max_people = (int) (long) result.get(i).get("max_people");
                List<String> joined_uid = (List<String>) result.get(i).get("joined_uid");
                String nowmax = joined_uid.size() + "/" + max_people;

                int dis = (int) distance(loc.getLatitude(), loc.getLongitude(), (double) result.get(i).get("departureX") , (double) result.get(i).get("departureY"));
                adapter.addItem(new NearList(UidNicknameData.getUidNicknameMap().get((String) result.get(i).get("author_uid")),
                        result.get(i).get("departure") + "",
                        result.get(i).get("arrival") + "",
                        dis + "m",
                        (int) (long) result.get(i).get("gender"),
                        nowmax));
                listView.setAdapter(adapter);
            }
        });



        // 리스트뷰 클릭 이벤트
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String uid = (String) result.get(position).get("uid");
                String departure = (String) result.get(position).get("departure");
                String arrival = (String) result.get(position).get("arrival");
                String departureAddress = (String) result.get(position).get("departure_address");
                String arrivalAddress = (String) result.get(position).get("arrival_address");
                int max_people = (int) (long) result.get(position).get("max_people");
                List<String> joined_uid = (List<String>) result.get(position).get("joined_uid");
                String nowmax = joined_uid.size() + "/" + max_people;
                String departureX = result.get(position).get("departureX") + "";
                String departureY = result.get(position).get("departureY") + "";

                Intent intent = new Intent(getContext(), RoomInformationActivity.class);
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

        return view;
    }

    //버튼 이벤트 처리
    public void onClick(View view) {
        if(view.getId() == R.id.refresh) {
            getRoomList(inIsTaskDone -> {
                adapter = new NearListAdapter();
                for (int i = 0; i < result.size(); i++) {
                    int max_people = (int) (long) result.get(i).get("max_people");
                    List<String> joined_uid = (List<String>) result.get(i).get("joined_uid");
                    String nowmax = joined_uid.size() + "/" + max_people;
                    Location loc = getCurrentLocation(getContext());
                    int dis = (int) distance(loc.getLatitude(), loc.getLongitude(), (double) result.get(i).get("departureX") , (double) result.get(i).get("departureY"));
                    adapter.addItem(new NearList(UidNicknameData.getUidNicknameMap().get((String) result.get(i).get("author_uid")),
                            result.get(i).get("departure") + "",
                            result.get(i).get("arrival") + "",
                            dis + "m",
                            (int) (long) result.get(i).get("gender"),
                            nowmax));
                    listView.setAdapter(adapter);
                }
            });
        }
    }

    public void getRoomList(final isTaskDoneCallback callback) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        CollectionReference partyRef = firestore.collection("TagoParty");

        partyRef.get().addOnCompleteListener(task -> {

            Location loc = getCurrentLocation(getContext());

            if (task.isSuccessful()) {
                result = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Double departureX = (Double) document.getData().get("departureX");
                    Double departureY = (Double) document.getData().get("departureY");

                    if(distance(loc.getLatitude(), loc.getLongitude(), departureX, departureY) <= 500) {
                        Map<String, Object> tempParty = new HashMap<>();
                        tempParty.put("uid", document.getId());
                        tempParty.put("author_uid", document.getData().get("author_uid"));
                        tempParty.put("departure", document.getData().get("departure"));
                        tempParty.put("departure_address", document.getData().get("departure_address"));
                        tempParty.put("arrival", document.getData().get("arrival"));
                        tempParty.put("arrival_address", document.getData().get("arrival_address"));
                        tempParty.put("departureX", document.getData().get("departureX"));
                        tempParty.put("departureY", document.getData().get("departureY"));
                        tempParty.put("arrivalX", document.getData().get("arrivalX"));
                        tempParty.put("arrivalY", document.getData().get("arrivalY"));
                        tempParty.put("max_people", document.getData().get("max_people"));
                        tempParty.put("gender", document.getData().get("gender"));
                        tempParty.put("joined_uid", document.getData().get("joined_uid"));
                        tempParty.put("date", document.getData().get("date"));

                        // 값 넣기
                        result.add(tempParty);


                        // 출력 확인
                        for(String key : tempParty.keySet()) {
                            Object value = tempParty.get(key);
                            System.out.println(key + " : " + value);
                        }
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

}

//        Button signoutButton = (Button) view.findViewById(R.id.signoutButton);
//        Button testButton = (Button) view.findViewById(R.id.testButton);
//
//        signoutButton.setOnClickListener(this);
//        testButton.setOnClickListener(this);
//
//        switch (view.getId()) {
//            case R.id.signoutButton:
//                FirebaseAuth.getInstance().signOut();
//                FragmentActivity f = getActivity();
//                Intent intent = new Intent(f, LoginActivity.class);
//                startActivity(intent);
//                f.finish();
//
//            case R.id.testButton:
////                방 만드는 편법
////                FragmentActivity d = getActivity();
////                Intent intent2 = new Intent(d, CreateNewRoomActivity.class);
////                startActivity(intent2);
//                new Thread (() -> {
//                    String clientId = "xz03is1zp8"; //애플리케이션 클라이언트 아이디값"
//                    String clientSecret = "Ypa9kuJHBBilpKhF0HhjPCT4zU3MPLclvuAy6EF2"; //애플리케이션 클라이언트 시크릿값"
//
//                    String text = null;
//                    try {
//                        text = URLEncoder.encode("그린팩토리", "UTF-8");
//                    } catch (UnsupportedEncodingException e) {
//                        throw new RuntimeException("검색어 인코딩 실패", e);
//                    }
//
//                    String apiURL = "https://naveropenapi.apigw.ntruss.com/map-reversegeocode/v2/gc?coords=127.0644347,37.2856370&sourcecrs=epsg:4326&orders=roadaddr&output=json";    // json 결과
//
//                    Map<String, String> requestHeaders = new HashMap<>();
//                    requestHeaders.put("X-NCP-APIGW-API-KEY-ID", clientId);
//                    requestHeaders.put("X-NCP-APIGW-API-KEY", clientSecret);
//                    String responseBody = get(apiURL, requestHeaders);
//
//                    System.out.println(responseBody);
//                }).start();
//        }
//    }
//
//    private String get(String apiUrl, Map<String, String> requestHeaders) {
//        HttpURLConnection con = connect(apiUrl);
//        try {
//            con.setRequestMethod("GET");
//            for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
//                con.setRequestProperty(header.getKey(), header.getValue());
//            }
//
//
//            int responseCode = con.getResponseCode();
//            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
//                return readBody(con.getInputStream());
//            } else { // 에러 발생
//                return readBody(con.getErrorStream());
//            }
//        } catch (IOException e) {
//            throw new RuntimeException("API 요청과 응답 실패", e);
//        } finally {
//            con.disconnect();
//        }
//    }
//
//    private static HttpURLConnection connect(String apiUrl){
//        try {
//            URL url = new URL(apiUrl);
//            return (HttpURLConnection)url.openConnection();
//        } catch (MalformedURLException e) {
//            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
//        } catch (IOException e) {
//            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
//        }
//    }
//
//    private static String readBody(InputStream body){
//        InputStreamReader streamReader = new InputStreamReader(body);
//
//
//        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
//            StringBuilder responseBody = new StringBuilder();
//
//
//            String line;
//            while ((line = lineReader.readLine()) != null) {
//                responseBody.append(line);
//            }
//
//
//            return responseBody.toString();
//        } catch (IOException e) {
//            throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
//        }
//    }
//
//}