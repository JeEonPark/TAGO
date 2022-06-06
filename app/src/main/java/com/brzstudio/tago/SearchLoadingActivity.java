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

        double coef = 0.00451;
        double lon = 0.00449;

        double departureLowerX = DepartureArrivalData.getDepartureX() - coef;
        double departureLowerY = DepartureArrivalData.getDepartureY() - coef / Math.cos(DepartureArrivalData.getDepartureX() * 0.018);
        double departureGreaterX = DepartureArrivalData.getDepartureX() + coef;
        double departureGreaterY = DepartureArrivalData.getDepartureY() + coef / Math.cos(DepartureArrivalData.getDepartureX() * 0.018);

        double arrivalLowerX = DepartureArrivalData.getArrivalX() - coef;
        double arrivalLowerY = DepartureArrivalData.getArrivalY() - coef / Math.cos(DepartureArrivalData.getDepartureX() * 0.018);
        double arrivalGreaterX = DepartureArrivalData.getArrivalX() + coef;
        double arrivalGreaterY = DepartureArrivalData.getArrivalY() + coef / Math.cos(DepartureArrivalData.getDepartureX() * 0.018);

        public void run() {
            System.out.println(departureLowerX + " | " + departureGreaterX + " | " + departureLowerY + " | " + departureGreaterY);
            firestore = FirebaseFirestore.getInstance();
            System.out.println("-----------Run----------");
            CollectionReference partyRef = firestore.collection("TagoParty");
            Query q1 = partyRef
                    .whereGreaterThan("departureX", departureLowerX)
                    .whereLessThan("departureX", departureGreaterX);

            q1.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        System.out.println("-----------Succ----------");
                        Double departureY = (Double) document.getData().get("departureY");

                        if(departureY > departureLowerY && departureY < departureGreaterY) {
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
}