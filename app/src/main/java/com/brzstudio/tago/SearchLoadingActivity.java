package com.brzstudio.tago;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SearchLoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_loading);

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(getApplicationContext(), CreateNewRoomActivity.class);
            startActivity(intent);
            finish();
        }, 3000);



    }
}