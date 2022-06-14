package com.brzstudio.tago;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CreateNewRoomActivity extends AppCompatActivity {

    LinearLayout topBar;
    TextView departureTextView;
    TextView departureAddressTextView;
    TextView arrivalTextView;
    TextView arrivalAddressTextView;
    Button createButton;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    RadioGroup radioGroup;
    CheckBox sameGenderCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_room);

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        radioGroup = findViewById(R.id.radioGroup);
        sameGenderCheckBox = findViewById(R.id.sameGenderCheckBox);

        // topBar에서 상단바만큼 margin 추가
        topBar = findViewById(R.id.topBar);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        params.setMargins(0, getStatusBarHeight(this), 0, 0);
        topBar.setLayoutParams(params);

        // 출발지 도착지 TextView 입력된 정보로 변경
        departureTextView = findViewById(R.id.departureTextView);
        departureAddressTextView = findViewById(R.id.departureAddressTextView);
        arrivalTextView = findViewById(R.id.arrivalTextView);
        arrivalAddressTextView = findViewById(R.id.arrivalAddressTextView);

        departureTextView.setText(DepartureArrivalData.getDeparture());
        departureAddressTextView.setText(DepartureArrivalData.getDepartureAddress());
        arrivalTextView.setText(DepartureArrivalData.getArrival());
        arrivalAddressTextView.setText(DepartureArrivalData.getArrivalAddress());

        // 방 생성 버튼 클릭 시
        createButton = findViewById(R.id.createRoom);
        createButton.setOnClickListener(view -> {
            DocumentReference documentReference = firestore.collection("TagoParty").document();

            if(getCheckBox() == 0) {
                AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
                alert.setMessage("최대 인원 수를 선택해주세요.");
                // 버튼 추가 (Ok 버튼과 Cancle 버튼 )
                alert.setPositiveButton("확인", (dialog, which) -> {
                    // OK 버튼을 눌렸을 경우
                    System.out.println("ok");
                });
                // Alert를 생성해주고 보여주는 메소드(show를 선언해야 Alert가 생성됨)
                alert.show();
            } else {
                int gen = 0;
                if(sameGenderCheckBox.isChecked()) {
                    gen = LoginedUserData.getGender();
                }
                Map<String, Object> partyInfo = new HashMap<>();
                partyInfo.put("author_uid", firebaseAuth.getCurrentUser().getUid());
                partyInfo.put("departure", DepartureArrivalData.getDeparture());
                partyInfo.put("departure_address", DepartureArrivalData.getDepartureAddress());
                partyInfo.put("arrival", DepartureArrivalData.getArrival());
                partyInfo.put("arrival_address", DepartureArrivalData.getArrivalAddress());
                partyInfo.put("departureX", DepartureArrivalData.getDepartureX());
                partyInfo.put("departureY", DepartureArrivalData.getDepartureY());
                partyInfo.put("arrivalX", DepartureArrivalData.getArrivalX());
                partyInfo.put("arrivalY", DepartureArrivalData.getArrivalY());
                partyInfo.put("max_people", getCheckBox());
                partyInfo.put("gender", gen);
                partyInfo.put("joined_uid", Arrays.asList(firebaseAuth.getCurrentUser().getUid()));
                partyInfo.put("date", new Timestamp(new Date()));

                documentReference.set(partyInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        System.out.println("Success");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println(e);
                    }
                });
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

    //2명 3명 4명 체크 박스 확인 함수
    public int getCheckBox() {
        RadioButton p2 = (RadioButton) findViewById(R.id.radio2);
        RadioButton p3 = (RadioButton) findViewById(R.id.radio3);
        RadioButton p4 = (RadioButton) findViewById(R.id.radio4);

        if(p2.isChecked()) {
            return 2;
        } else if (p3.isChecked()){
            return 3;
        } else if (p4.isChecked()) {
            return 4;
        }

        return 0;
    }
}