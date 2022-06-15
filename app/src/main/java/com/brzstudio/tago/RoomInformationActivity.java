package com.brzstudio.tago;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;

public class RoomInformationActivity extends AppCompatActivity implements OnMapReadyCallback {

    LinearLayout topBar;
    TextView departureTextView;
    TextView departureAddressTextView;
    TextView arrivalTextView;
    TextView arrivalAddressTextView;
    TextView nowmaxTextView;

    Button joinRoomButton;

    MapView mapViewFragment;
    NaverMap naverMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_information);

        // topBar에서 상단바만큼 margin 추가
        topBar = findViewById(R.id.topBar);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        params.setMargins(0, getStatusBarHeight(this), 0, 0);
        topBar.setLayoutParams(params);

        // 지도 설정
        //네이버지도 관련
        mapViewFragment = (MapView) findViewById(R.id.map_view_fragment);
        mapViewFragment.onCreate(savedInstanceState);
        mapViewFragment.getMapAsync(this);

        departureTextView = findViewById(R.id.departureTextView);
        departureAddressTextView = findViewById(R.id.departureAddressTextView);
        arrivalTextView = findViewById(R.id.arrivalTextView);
        arrivalAddressTextView = findViewById(R.id.arrivalAddressTextView);
        nowmaxTextView = findViewById(R.id.nowmaxTextView);

        joinRoomButton = findViewById(R.id.joinRoom);

        //이전 액티비티 intent 전달 값 받아오기
        Intent intendData = getIntent();
        String uid = intendData.getStringExtra("uid");
        String departure = intendData.getStringExtra("departure");
        String arrival = intendData.getStringExtra("arrival");
        String departureAddress = intendData.getStringExtra("departureAddress");
        String arrivalAddress = intendData.getStringExtra("arrivalAddress");
        String nowmax = intendData.getStringExtra("nowmax");
        String departureX = intendData.getStringExtra("departureX");
        String departureY = intendData.getStringExtra("departureY");

        departureTextView.setText(departure);
        arrivalTextView.setText(arrival);
        departureAddressTextView.setText(departureAddress);
        arrivalAddressTextView.setText(arrivalAddress);
        nowmaxTextView.setText("현재 참여 인원 : " + nowmax);

        // 같이 탑승하기 버튼 이벤트
        joinRoomButton.setOnClickListener(view -> {
            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
            firestore.collection("TagoParty").document(uid).update("joined_uid", FieldValue.arrayUnion(LoginedUserData.getUid()));

            Intent intent = new Intent(getApplicationContext(), ChatRoomActivity.class);
            intent.putExtra("uid", uid);

            startActivity(intent);

        });


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

    //네이버지도 관련 함수-------------------------------------------------------------------------
    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        CameraPosition cameraPosition = new CameraPosition(new LatLng(DepartureArrivalData.getDepartureX(),DepartureArrivalData.getDepartureY()), 16);
        naverMap.setCameraPosition(cameraPosition);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        mapViewFragment.onStart();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        mapViewFragment.onResume();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        mapViewFragment.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        mapViewFragment.onSaveInstanceState(outState);
    }

    @Override
    public void onStop()
    {
        super.onStop();
        mapViewFragment.onStop();
    }

    @Override
    public void onLowMemory()
    {
        super.onLowMemory();
        mapViewFragment.onLowMemory();
    }


}