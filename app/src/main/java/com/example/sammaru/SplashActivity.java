package com.example.sammaru;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.example.sammaru.login.LoginActivity;

/**
 * 이 액티비티에서는 구글 파이어베이스에 계정이 있는지 확인하고
 * 계정이 있으면 자동 로그인 후 MainActivity로 넘어가게 하고
 * 없다면 LoginActivity로 넘어가서 회원가입 또는 로그인을 진행하도록 한다.
 *
 * Google Firebase 연동 필요
 * */

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        // 전체화면 설정
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        // LoginActivity 실행
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();

    }
}
