package com.example.sammaru;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.sammaru.courier.CourierMainActivity;
import com.example.sammaru.customer.CustomerMainActivity;
import com.example.sammaru.login.LoginActivity;
import com.example.sammaru.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * 스플래시 액티비티
 * 기능 : 자동 로그인
 * */

public class SplashActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;    // 아이디와 비밀번호 저장

    private String email;
    private String password;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // 전체화면 설정
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // 자동 로그인 데이터 불러옴
        sharedPreferences = getSharedPreferences("loginFile", MODE_PRIVATE);
        email = sharedPreferences.getString("email", "");
        password = sharedPreferences.getString("password", "");

        // 자동 로그인 처리
        if (!email.contentEquals("") && !password.contentEquals("")) {
            loginEvent();
        } else {
            // LoginActivity 실행
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    // loginEvent : email, password 확인 후 배달원, 고객으로 나눠서 자동 로그인
    private void loginEvent() {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            final String uid = task.getResult().getUser().getUid();
                            databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot item : dataSnapshot.getChildren()) {
                                        UserModel userModel = item.getValue(UserModel.class);
                                        if (userModel.getUid().equals(uid)) {
                                            int loginIdentifier = userModel.getIdentifier();
                                            chooseActivity(loginIdentifier);
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                        else {
                            Toast.makeText(SplashActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // chooseActivity : 배달원, 고객 나눠서 MainActivity 실행
    private void chooseActivity(int loginIdentifier) {
        switch (loginIdentifier) {
            case 1:
                // CustomerMainActivity 실행
                startActivity(new Intent(this, CustomerMainActivity.class));
                finish();
                break;

            case 2:
                // CourierMainActivity 실행
                startActivity(new Intent(this, CourierMainActivity.class));
                finish();
                break;

            default:
                break;
        }
    }
}
