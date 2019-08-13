package com.example.sammaru.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private int loginIdentifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        id = findViewById(R.id.login_activity_editText_id);
        password = findViewById(R.id.login_activity_editText_password);

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

    private void loginEvent() {
        if (id.getText().toString().contentEquals("") || password.getText().toString().contentEquals("")) {
            Toast.makeText(this, "아이디와 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

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
                                            loginIdentifier = userModel.getIdentifier();
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
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

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
}
