package com.example.sammaru.login;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.sammaru.R;
import com.example.sammaru.model.CustomerModel;
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
                // 회원가입 양식을 빼먹었을 경우 회원가입 안됨
                if (email.getText().toString() == null || pw.getText().toString() == null || name.getText().toString() == null || phone.getText().toString() == null) {
                    return;
                }

                FirebaseAuth.getInstance()
                        .createUserWithEmailAndPassword(email.getText().toString(), pw.getText().toString())
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                final String uid = task.getResult().getUser().getUid();
                                CustomerModel customerModel = new CustomerModel();
                                customerModel.setName(name.getText().toString());
                                customerModel.setUid(uid);
                                customerModel.setPhone(phone.getText().toString());

                                FirebaseDatabase.getInstance().getReference().child("users").child("customers").child(uid).setValue(customerModel)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
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
