package com.example.sammaru.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sammaru.R;
import com.example.sammaru.courier.CourierMainActivity;
import com.example.sammaru.customer.CustomerMainActivity;
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

public class LoginActivity extends AppCompatActivity {

    private EditText id;
    private EditText password;
    private CheckBox autoLogin;
    private CheckBox rememberEmail;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private int loginIdentifier;    // 로그인 구분자 1:고객, 2:배달원

    private SharedPreferences sharedPreferences;    // 이메일 기억에 사용

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences("loginFile", MODE_PRIVATE);

        id = findViewById(R.id.login_activity_editText_id);
        password = findViewById(R.id.login_activity_editText_password);
        autoLogin = findViewById(R.id.activity_login_auto_login_checkbox);
        rememberEmail = findViewById(R.id.activity_login_remember_email_checkbox);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // 회원가입 버튼 클릭 이벤트
        Button join = findViewById(R.id.login_activity_button_join);
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, JoinActivity.class));
            }
        });

        // 로그인 버튼 클릭 이벤트
        Button login = findViewById(R.id.login_activity_button_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginEvent();
            }
        });
    }

    // loginEvent : 서버에서 id, pw 검사 후 고객, 배달원 구분해서 mainActivity 실행
    private void loginEvent() {
        // id, pw 공백 검사
        if (id.getText().toString().contentEquals("") || password.getText().toString().contentEquals("")) {
            Toast.makeText(this, "아이디와 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 서버에서 id, pw 정보 검사
        firebaseAuth.signInWithEmailAndPassword(id.getText().toString(), password.getText().toString())
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
                                            loginIdentifier = userModel.getIdentifier(); // 구분자 1: 고객, 2: 배달원
                                            chooseActivity(loginIdentifier);    // 구분자에 따른 메인 액티비티 선택
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                        else {
                            // 로그인 실패시 원인을 보여줌
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // chooseActivity : MainActivity 선택
    private void chooseActivity(int loginIdentifier) {
        switch (loginIdentifier) {
            case 1:
                // CustomerMainActivity 실행
                startActivity(new Intent(LoginActivity.this, CustomerMainActivity.class));
                finish();
                break;

            case 2:
                // CourierMainActivity 실행
                startActivity(new Intent(LoginActivity.this, CourierMainActivity.class));
                finish();
                break;

                default:
                    break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        // 이메일 기억 체크시 이메일을 불러옴
        if(sharedPreferences.getString("remember", "").contentEquals("1")) {
            id.setText(sharedPreferences.getString("email", ""));
            rememberEmail.setChecked(true);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences.Editor editor = sharedPreferences.edit();

        // 자동 로그인 체크
        if (autoLogin.isChecked()) {
            String email = id.getText().toString();
            editor.putString("email", email);
            String password = this.password.getText().toString();
            editor.putString("password", password);
            editor.commit();
        }

        // 이메일 기억 체크
        if (rememberEmail.isChecked()) {
            String email = id.getText().toString();
            editor.putString("email", email);
            editor.putString("remember", "1");
            editor.commit();
        }

    }
}
