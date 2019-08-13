package com.example.sammaru.login;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sammaru.R;
import com.example.sammaru.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class CustomerJoinFragment extends Fragment {

    private EditText email;
    private EditText pw;
    private EditText name;
    private EditText phone;
    private Button submit;
    private Button cancel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_customer_join, container, false);

        email = rootView.findViewById(R.id.fragment_customer_join_email);
        pw = rootView.findViewById(R.id.fragment_customer_join_password);
        name = rootView.findViewById(R.id.fragment_customer_join_name);
        phone = rootView.findViewById(R.id.fragment_customer_join_phone);
        submit = rootView.findViewById(R.id.fragment_customer_join_submit);
        cancel = rootView.findViewById(R.id.fragment_customer_join_cancel);

        // 회원가입 버튼 클릭 이벤트
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 회원가입 양식을 빼먹었을 경우 회원가입 안됨, 실패시 안되는 이유 설명 추가
                if (email.getText().toString().contentEquals("") || pw.getText().toString().contentEquals("")
                        || name.getText().toString().contentEquals("") || phone.getText().toString().contentEquals("")) {
                    Toast.makeText(getActivity(), "양식을 다 채워주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(getContext(), "회원가입 처리중...", Toast.LENGTH_SHORT).show();

                FirebaseAuth.getInstance()
                        .createUserWithEmailAndPassword(email.getText().toString(), pw.getText().toString())
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                final String uid = task.getResult().getUser().getUid();
                                UserModel userModel = new UserModel();
                                userModel.setUid(uid);
                                userModel.setName(name.getText().toString());
                                userModel.setPhone(phone.getText().toString());
                                userModel.setIdentifier(1); // 고객일 경우 구분자 1

                                FirebaseDatabase.getInstance().getReference().child("users").child(uid).setValue(userModel)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(getContext(), "회원가입 성공!", Toast.LENGTH_SHORT).show();
                                                getActivity().finish();
                                            }
                                        });

                            }
                        });
            }
        });

        // 취소 버튼 이벤트
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        return rootView;
    }

}
