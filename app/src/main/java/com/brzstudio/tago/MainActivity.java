package com.brzstudio.tago;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.naver.maps.map.NaverMapSdk;

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

        NaverMapSdk.getInstance(this).setClient(new NaverMapSdk.NaverCloudPlatformClient("xz03is1zp8"));



        // 바텀 네비게이션
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // 초기 플래그먼트 설정
        setFragment("find", fragment_find);


        // 리스너 등록
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Log.i(TAG, "바텀 네비게이션 클릭");

                switch (item.getItemId()){
                    case R.id.find:
                        Log.i(TAG, "Find 들어옴");
                        setFragment("find", fragment_find);
                        return true;
                    case R.id.list:
                        Log.i(TAG, "List 들어옴");
                        setFragment("list", fragment_list);
                        return true;
                    case R.id.chat:
                        Log.i(TAG, "Chat 들어옴");
                        setFragment("chat", fragment_chat);
                        return true;
                    case R.id.setting:
                        Log.i(TAG, "Setting 들어옴");
                        setFragment("setting", fragment_setting);
                        return true;
                }
                return true;
            }
        });
    }

    public void setFragment(String tag, Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();

        if (manager.findFragmentByTag(tag) == null) {
            ft.add(R.id.main_layout, fragment, tag);
        }

        Fragment find = manager.findFragmentByTag("find");
        Fragment list = manager.findFragmentByTag("list");
        Fragment chat = manager.findFragmentByTag("chat");
        Fragment setting = manager.findFragmentByTag("setting");

        if(find != null) {
            ft.hide(find);
        }
        if (list != null) {
            ft.hide(list);
        }
        if (chat != null) {
            ft.hide(chat);
        }
         if (setting != null) {
            ft.hide(setting);
        }

        if (tag == "find") {
            if (find != null) {
                ft.show(find);
            }
        }
        if (tag == "list") {
            if (list != null) {
                ft.show(list);
            }
        }
        if (tag == "chat") {
            if (chat != null) {
                ft.show(chat);
            }
        }
        if (tag == "setting") {
            if (setting != null) {
                ft.show(setting);
            }
        }

        ft.commitAllowingStateLoss();


    }

    public void onChangeFragment(int index){
        if(index == 0){
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment_find).commit();
        }
    }
}