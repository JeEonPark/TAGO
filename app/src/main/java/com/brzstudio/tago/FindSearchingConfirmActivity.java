package com.brzstudio.tago;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.geometry.Tm128;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;

public class FindSearchingConfirmActivity extends AppCompatActivity implements OnMapReadyCallback {

    MapView mapViewFragment;
    TextView locationTextView;
    TextView addressTextView;
    NaverMap naverMap;
    Tm128 tm;
    Button findButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_searching_confirm);

        locationTextView = findViewById(R.id.locationTextView);
        addressTextView = findViewById(R.id.addressTextView);

        findButton = findViewById(R.id.findButton);

        //이전 액티비티 intent 전달 값 받아오기
        Intent intent = getIntent();
        String location = intent.getStringExtra("location");
        String address = intent.getStringExtra("address");
        String mapX = intent.getStringExtra("mapX");
        String mapY = intent.getStringExtra("mapY");
        String isDepApp = intent.getStringExtra("isDepApp");


        locationTextView.setText(location);
        addressTextView.setText(address);

        //카텍 좌표계에서 위경도 변환
        tm = new Tm128(Integer.parseInt(mapX), Integer.parseInt(mapY));

        //화면 크기에 맞게 지도 크기 변경
        setMapSize();

        //네이버지도 관련
        mapViewFragment = (MapView) findViewById(R.id.map_view_fragment);
        mapViewFragment.onCreate(savedInstanceState);
        mapViewFragment.getMapAsync(this);

        //버튼 출발지 도착지 바꾸기
        if(isDepApp.equals("Dep")){
            findButton.setText("출발지로 설정");
        } else if (isDepApp.equals("App")){
            findButton.setText("도착지로 설정");
        }

        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isDepApp.equals("Dep")){
                    DepartureArrivalData.setDoneDeparture(true);
                    DepartureArrivalData.setDeparture(location);
                    DepartureArrivalData.setDepartureX(tm.toLatLng().latitude);
                    DepartureArrivalData.setDepartureY(tm.toLatLng().longitude);



                    setResult(100);
                } else if(isDepApp.equals("App")) {
                    DepartureArrivalData.setDoneArrival(true);
                    DepartureArrivalData.setArrival(location);
                    DepartureArrivalData.setArrivalX(tm.toLatLng().latitude);
                    DepartureArrivalData.setArrivalY(tm.toLatLng().longitude);

                    setResult(100);
                }

                finish();

            }
        });
    }


    //맵 크기 관련 함수-------------------------------------------------------------------------
    public void setMapSize() {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int widthPixels = metrics.widthPixels;
        int heightPixels = metrics.heightPixels - dpToPx(130);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(widthPixels, heightPixels);

        findViewById(R.id.map_view).setLayoutParams(params);

    }


    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public int pxToDp(int px) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    //네이버지도 관련 함수-------------------------------------------------------------------------
    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        CameraPosition cameraPosition = new CameraPosition(tm.toLatLng(), 16);
        naverMap.setCameraPosition(cameraPosition);
        System.out.println(tm.toLatLng().latitude);
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