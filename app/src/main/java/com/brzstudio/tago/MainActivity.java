package com.brzstudio.tago;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    // 바텀 네비게이션
    BottomNavigationView bottomNavigationView;

    private String TAG = "메인";

    // 프래그먼트 변수
    Fragment fragment_find;
    Fragment fragment_list;
    Fragment fragment_chat;
    Fragment fragment_setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 프래그먼트 생성
        fragment_find = new FindFragment();
        fragment_list = new ListFragment();
        fragment_chat = new ChatFragment();
        fragment_setting = new SettingFragment();


        // 바텀 네비게이션
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // 초기 플래그먼트 설정
        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, fragment_find).commitAllowingStateLoss();


        // 바텀 네비게이션
        bottomNavigationView = findViewById(R.id.bottomNavigationView);


        // 리스너 등록
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Log.i(TAG, "바텀 네비게이션 클릭");

                switch (item.getItemId()){
                    case R.id.find:
                        Log.i(TAG, "Find 들어옴");
                        getSupportFragmentManager().beginTransaction() .replace(R.id.main_layout,fragment_find).commitAllowingStateLoss();
                        return true;
                    case R.id.list:
                        Log.i(TAG, "List 들어옴");
                        getSupportFragmentManager().beginTransaction() .replace(R.id.main_layout,fragment_list).commitAllowingStateLoss();
                        return true;
                    case R.id.chat:
                        Log.i(TAG, "Chat 들어옴");
                        getSupportFragmentManager().beginTransaction() .replace(R.id.main_layout,fragment_chat).commitAllowingStateLoss();
                        return true;
                    case R.id.setting:
                        Log.i(TAG, "Setting 들어옴");
                        getSupportFragmentManager().beginTransaction() .replace(R.id.main_layout,fragment_setting).commitAllowingStateLoss();
                        return true;
                }
                return true;
            }
        });
    }

    public void onChangeFragment(int index){
        if(index == 0){
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment_find).commit();
        }
    }
}