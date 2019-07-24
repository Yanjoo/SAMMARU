package com.example.sammaru.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.sammaru.R;
import com.example.sammaru.customer.CustomerMainActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 회원가입 버튼 클릭 이벤트
        Button join = findViewById(R.id.login_activity_button_join);
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // JoinActivity 실행
                startActivity(new Intent(LoginActivity.this, JoinActivity.class));
            }
        });

        // 로그인 버튼 클릭 이벤트
        Button login = findViewById(R.id.login_activity_button_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // DeliveryMainActivity 실행
                startActivity(new Intent(LoginActivity.this, CustomerMainActivity.class));
                finish();
            }
        });
    }
}
