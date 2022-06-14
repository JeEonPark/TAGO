package com.brzstudio.tago;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainLoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_loading);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        //이미 로그인되어있으면 넘겨줌
        if(firebaseAuth.getCurrentUser() != null) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            getUserData(user.getUid(), inIsTaskDone -> {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            });
        } else {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void getUserData(String uid, final LoginActivity.isTaskDoneCallback callback) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        DocumentReference userData = firestore.collection("UserInformation").document(uid);

        userData.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                DocumentSnapshot document = task.getResult();
                if(document.exists()){
                    Log.d(TAG, "Logined user data : " + document.getData());
                    LoginedUserData.setEmail((String) document.getData().get("email"));
                    LoginedUserData.setLongGender((Long) document.getData().get("gender"));
                    LoginedUserData.setNickname((String) document.getData().get("nickname"));
                } else {
                    Log.d(TAG, "No such document");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
            callback.onCallback(true);

        });
    }
}