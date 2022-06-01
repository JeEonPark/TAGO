package com.brzstudio.tago;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.firebase.auth.FirebaseAuth;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.util.FusedLocationSource;


public class FindFragment extends Fragment implements View.OnClickListener, OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private NaverMap naverMap;
    private FusedLocationSource locationSource;
    private MapView mapViewFragment;

    public FindFragment() { }

    public static FindFragment newInstance()
    {
        FindFragment fragment = new FindFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_find, container, false);

        //화면 크기에 맞게 지도 크기 변경
        setMapSize(view);

        //네이버 지도 현재위치
        mapViewFragment = (MapView) view.findViewById(R.id.map_view_fragment);
        mapViewFragment.onCreate(savedInstanceState);
        mapViewFragment.getMapAsync(this);
        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);

        Button departureButton = (Button) view.findViewById(R.id.departureButton);
        Button arrivalButton = (Button) view.findViewById(R.id.arrivalButton);
        departureButton.setOnClickListener(this);
        arrivalButton.setOnClickListener(this);

        return view;
    }


    //버튼 이벤트 처리
    public void onClick(View view) {
        if(view.getId() == R.id.departureButton || view.getId() == R.id.arrivalButton) {
            FragmentActivity f = getActivity();
            Intent intent = new Intent(f, FindSearchingActivity.class);
            startActivityIntent.launch(intent);
        }
    }

    //출발지 목적지
    final ActivityResultLauncher<Intent> startActivityIntent = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == 100) {

                    }
                }
            });

    //지도 관련----------------------------------------------------------------------------
    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        naverMap.setLocationSource(locationSource);
        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
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
    public void onDestroyView()
    {
        super.onDestroyView();
        mapViewFragment.onDestroy();
    }

    @Override
    public void onLowMemory()
    {
        super.onLowMemory();
        mapViewFragment.onLowMemory();
    }

    public void setMapSize(View view) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int widthPixels = metrics.widthPixels;
        int heightPixels = metrics.heightPixels - dpToPx(290);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(widthPixels, heightPixels);

        view.findViewById(R.id.map_view).setLayoutParams(params);

    }


    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public int pxToDp(int px) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

}