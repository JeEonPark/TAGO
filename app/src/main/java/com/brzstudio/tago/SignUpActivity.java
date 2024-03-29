package com.brzstudio.tago;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    private EditText emailTextEdit;
    private EditText passwordTextEdit;
    private EditText passwordCheckTextEdit;
    private EditText nicknameTextEdit;
    private Button signUpButton;
    private RadioGroup genderRadioGroup;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        TextView signUpAlert = (TextView)findViewById(R.id.signUpAlert);

        emailTextEdit = (EditText) findViewById(R.id.emailTextEdit);
        passwordTextEdit = (EditText) findViewById(R.id.passwordTextEdit);
        passwordCheckTextEdit = (EditText) findViewById(R.id.passwordCheckTextEdit);
        nicknameTextEdit = (EditText) findViewById(R.id.nicknameTextEdit);
        signUpButton = (Button) findViewById(R.id.signUpButton);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        signUpButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String email = emailTextEdit.getText().toString().trim();
                String password = passwordTextEdit.getText().toString().trim();
                String passwordCheck = passwordCheckTextEdit.getText().toString().trim();
                String nickname = nicknameTextEdit.getText().toString().trim();

                if(email.getBytes().length <= 0) {
                    signUpAlert.setText("이메일을 입력하세요.");
                    emailTextEdit.requestFocus();
                } else if (password.getBytes().length <= 0) {
                    signUpAlert.setText("비밀번호를 입력하세요.");
                    passwordTextEdit.requestFocus();
                } else if (passwordCheck.getBytes().length <= 0) {
                    signUpAlert.setText("비밀번호 확인을 입력하세요.");
                    passwordCheckTextEdit.requestFocus();
                } else if (nickname.getBytes().length <= 0) {
                    signUpAlert.setText("닉네임을 입력하세요.");
                    nicknameTextEdit.requestFocus();
                } else if (getCheckBox() == 0) {
                    signUpAlert.setText("성별을 선택해주세요.");
                } else {
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(password.equals(passwordCheck)) {
                                SignUpActivity.super.onStart();
                                FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                                if (currentUser != null) {
                                    currentUser.reload();
                                }
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "CreateUserWithEmail:success");
                                    FirebaseUser account = firebaseAuth.getCurrentUser();
                                    DocumentReference documentReference = firestore.collection("UserInformation").document(account.getUid());
                                    Map<String, Object> userInfo = new HashMap<>();
                                    userInfo.put("email", email);
                                    userInfo.put("nickname", nickname);
                                    userInfo.put("gender", getCheckBox());
                                    documentReference.set(userInfo);
                                    LoginedUserData.setEmail(email);
                                    LoginedUserData.setGender(getCheckBox());
                                    LoginedUserData.setNickname(nickname);
                                    LoginedUserData.setUid(account.getUid());
                                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Exception e = task.getException();
                                    if (e instanceof FirebaseAuthWeakPasswordException) {
                                        signUpAlert.setText("비밀번호를 7자리 이상 입력하세요.");
                                    } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
                                        signUpAlert.setText("이메일을 올바른 형식으로 입력하세요.");
                                    }
                                }
                            } else {
                                signUpAlert.setText("비밀번호가 일치하지 않습니다.");
                            }
                        }
                    });
                }
            }
        });

        TextView loginButton = (TextView) findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    //남녀 체크박스 1:남성 2:여성
    public int getCheckBox() {
        RadioButton man = (RadioButton) findViewById(R.id.manRadio);
        RadioButton woman = (RadioButton) findViewById(R.id.womanRadio);

        if(man.isChecked()) {
            return 1;
        } else if (woman.isChecked()){
            return 2;
        }

        return 0;
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