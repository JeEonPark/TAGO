package com.brzstudio.tago;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RoomInformationActivity extends AppCompatActivity {

    LinearLayout topBar;
    TextView departureTextView;
    TextView departureAddressTextView;
    TextView arrivalTextView;
    TextView arrivalAddressTextView;
    TextView nowmaxTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_information);

        // topBar에서 상단바만큼 margin 추가
        topBar = findViewById(R.id.topBar);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        params.setMargins(0, getStatusBarHeight(this), 0, 0);
        topBar.setLayoutParams(params);

        departureTextView = findViewById(R.id.departureTextView);
        departureAddressTextView = findViewById(R.id.departureAddressTextView);
        arrivalTextView = findViewById(R.id.arrivalTextView);
        arrivalAddressTextView = findViewById(R.id.arrivalAddressTextView);
        nowmaxTextView = findViewById(R.id.nowmaxTextView);

        //이전 액티비티 intent 전달 값 받아오기
        Intent intent = getIntent();
        String departure = intent.getStringExtra("departure");
        String arrival = intent.getStringExtra("arrival");
        String departureAddress = intent.getStringExtra("departureAddress");
        String arrivalAddress = intent.getStringExtra("arrivalAddress");
        String nowmax = intent.getStringExtra("nowmax");
        String departureX = intent.getStringExtra("departureX");
        String departureY = intent.getStringExtra("departureY");

        departureTextView.setText(departure);
        arrivalTextView.setText(arrival);
        departureAddressTextView.setText(departureAddress);
        arrivalAddressTextView.setText(arrivalAddress);
        nowmaxTextView.setText("현재 참여 인원 : " + nowmax);

    }

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