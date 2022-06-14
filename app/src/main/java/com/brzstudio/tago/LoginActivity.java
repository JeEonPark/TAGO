package com.brzstudio.tago;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthActionCodeException;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private EditText emailTextEdit;
    private EditText passwordTextEdit;
    private Button loginButton;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView signUpButton = (TextView) findViewById(R.id.signUpButton);
        TextView loginAlert = (TextView)findViewById(R.id.loginAlert);

        //회원가입 누를 시 회원가입 창 띄우기----------------------------------------
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });

        //Firebase Login 처리------------------------------------------------
        emailTextEdit = (EditText) findViewById(R.id.emailTextEdit);
        passwordTextEdit = (EditText) findViewById(R.id.passwordTextEdit);
        loginButton = (Button) findViewById(R.id.loginButton);

        firebaseAuth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailTextEdit.getText().toString().trim();
                String password = passwordTextEdit.getText().toString().trim();

                if (email.getBytes().length <= 0) {
                    loginAlert.setText("이메일을 입력하세요.");
                    emailTextEdit.requestFocus();
                } else if (password.getBytes().length <= 0) {
                    loginAlert.setText("비밀번호를 입력하세요.");
                    passwordTextEdit.requestFocus();
                } else {
                        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "signInWithEmail:success");
                                    FirebaseUser user = firebaseAuth.getCurrentUser();
                                    getUserData(user.getUid(), inIsTaskDone -> {
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    });
                                } else {
                                    {
                                        try {
                                            throw task.getException();
                                        } catch (FirebaseAuthInvalidCredentialsException e) {
                                            loginAlert.setText("입력하신 정보가 올바르지 않습니다.");
                                            emailTextEdit.requestFocus();
                                        } catch (FirebaseAuthInvalidUserException e) {
                                            loginAlert.setText("입력하신 정보가 올바르지 않습니다.");
                                            emailTextEdit.requestFocus();
                                        } catch (Exception e) {
                                            System.out.println(e);
                                        }
                                    }
                                }
                            }
                        });
                }
            }
        });

    }

    public void getUserData(String uid, final isTaskDoneCallback callback) {
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
                    LoginedUserData.setUid(uid);
                } else {
                    Log.d(TAG, "No such document");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
            callback.onCallback(true);

        });
    }

    public interface isTaskDoneCallback {
        void onCallback(boolean inIsTaskDone);
    }

    // editText 외부 클릭 시 포커스 해제
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View focusView = getCurrentFocus();
        if (focusView != null) {
            Rect rect = new Rect();
            focusView.getGlobalVisibleRect(rect);
            int x = (int) ev.getX(), y = (int) ev.getY();
            if (!rect.contains(x, y)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null)
                    imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
                focusView.clearFocus();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

}

