package com.brzstudio.tago;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SearchLoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_loading);

//        new Handler().postDelayed(() -> {
//            Intent intent = new Intent(getApplicationContext(), CreateNewRoomActivity.class);
//            startActivity(intent);
//            finish();
//        }, 3000);

        GetPartyData gld = new GetPartyData();
        gld.start();
        try {
            gld.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }


    // 내 출발지 목적지의 500m 이내에 매칭되는 파티 가져오기 (기준 : only 정사각형)
    public class GetPartyData extends Thread {
        FirebaseFirestore firestore;
        private ArrayList result = new ArrayList();

        public void run() {
            firestore = FirebaseFirestore.getInstance();
            System.out.println("-----------Run----------");
            CollectionReference partyRef = firestore.collection("TagoParty");

            partyRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        System.out.println("-----------Succ----------");
                        Double departureX = (Double) document.getData().get("departureX");
                        Double departureY = (Double) document.getData().get("departureY");
                        Double arrivalX = (Double) document.getData().get("arrivalX");
                        Double arrivalY = (Double) document.getData().get("arrivalY");

                        if(distance(DepartureArrivalData.getDepartureX(), DepartureArrivalData.getDepartureY(), departureX, departureY) <= 500
                                && distance(DepartureArrivalData.getArrivalX(),DepartureArrivalData.getArrivalY(), arrivalX, arrivalY) <= 500) {
                            Map<String, Object> tempParty = new HashMap<>();
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
                            tempParty.put("same_gender", document.getData().get("same_gender"));
                            tempParty.put("joined_uid", document.getData().get("joined_uid"));
                            tempParty.put("date", document.getData().get("date"));

                            for(String key : tempParty.keySet()) {
                                Object value = tempParty.get(key);
                                System.out.println(key + " : " + value);
                            }
                        }
                    }
                } else {
                    System.out.println("task0 : Error getting documents: " + task.getException());
                }
            });
        }
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