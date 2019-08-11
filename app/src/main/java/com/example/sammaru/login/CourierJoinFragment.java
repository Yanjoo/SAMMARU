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
import com.example.sammaru.model.CourierModel;
import com.example.sammaru.model.CustomerModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


public class CourierJoinFragment extends Fragment {

    private EditText email;
    private EditText password;
    private EditText name;
    private EditText phone;
    private Button submit;
    private Button cancel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_courier_join, container, false);

        email = rootView.findViewById(R.id.fragment_courier_join_email);
        password = rootView.findViewById(R.id.fragment_courier_join_password);
        name = rootView.findViewById(R.id.fragment_courier_join_name);
        phone = rootView.findViewById(R.id.fragment_courier_join_phone);
        submit = rootView.findViewById(R.id.fragment_courier_join_submit);
        cancel = rootView.findViewById(R.id.fragment_courier_join_cancel);

        // 회원가입 버튼 클릭 이벤트
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 회원가입 양식을 빼먹었을 경우 회원가입 안됨, 실패시 안된 이유 추가
                if (email.getText().toString() == null || password.getText().toString() == null || name.getText().toString() == null || phone.getText().toString() == null) {
                    return;
                }

                Toast.makeText(getContext(), "회원가입 처리중...", Toast.LENGTH_SHORT).show();

                FirebaseAuth.getInstance()
                        .createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                final String uid = task.getResult().getUser().getUid();
                                CourierModel courierModel = new CourierModel();
                                courierModel.setName(name.getText().toString());
                                courierModel.setUid(uid);
                                courierModel.setPhone(phone.getText().toString());


                                FirebaseDatabase.getInstance().getReference().child("users").child("couriers").child(uid).setValue(courierModel)
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
