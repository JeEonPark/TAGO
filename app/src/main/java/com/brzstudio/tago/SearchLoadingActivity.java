package com.brzstudio.tago;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchLoadingActivity extends AppCompatActivity {

    ArrayList<Map<String, Object>> result = new ArrayList<>();
    Boolean sameGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_loading);

        Intent intentGet = getIntent();
        sameGender = intentGet.getBooleanExtra("sameGender", true);


        getPartyData(inIsTaskDone -> {
            UidNicknameData.updateUid(inIsTaskDone2 -> {
                Intent intent = new Intent(getApplicationContext(), CreatedRoomListActivity.class);
                intent.putExtra("result", result);
                startActivity(intent);
                finish();
            });
        });

    }

    // 내 출발지 목적지의 500m 이내에 매칭되는 파티 가져오기 (기준 : only 정사각형)
    public void getPartyData(final isTaskDoneCallback callback) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        CollectionReference partyRef = firestore.collection("TagoParty");

        partyRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    System.out.println("-----------Succ----------");
                    Double departureX = (Double) document.getData().get("departureX");
                    Double departureY = (Double) document.getData().get("departureY");
                    Double arrivalX = (Double) document.getData().get("arrivalX");
                    Double arrivalY = (Double) document.getData().get("arrivalY");
                    long dbGender = (long) document.getData().get("gender");



                    if(distance(DepartureArrivalData.getDepartureX(), DepartureArrivalData.getDepartureY(), departureX, departureY) <= 500
                            && distance(DepartureArrivalData.getArrivalX(),DepartureArrivalData.getArrivalY(), arrivalX, arrivalY) <= 500) {

                        if(sameGender && dbGender == LoginedUserData.getGender()) {
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
                        } else if (!sameGender) {
                            if(dbGender == LoginedUserData.getGender() || dbGender == 0) {
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
                            }
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