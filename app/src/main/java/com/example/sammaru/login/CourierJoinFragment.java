package com.example.sammaru.login;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.sammaru.R;
import com.example.sammaru.model.ProductModel;
import com.example.sammaru.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

/**
 * 배달원 회원가입 프래그먼트
 *
 * 기능 : 배달원 회원가입 정보를 서버로 전달
 */

public class CourierJoinFragment extends Fragment {

    private EditText email;
    private EditText password;
    private EditText name;
    private EditText phone;
    private Button submit;
    private Button cancel;
    private Button choiceCompany;

    private UserModel userModel;

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
        choiceCompany = rootView.findViewById(R.id.fragment_courier_join_company);

        // 회사 선택 버튼 클릭 이벤트
        choiceCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        userModel = new UserModel();

        // 회원가입 버튼 클릭 이벤트
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 회원가입 양식을 빼먹었을 경우 회원가입 안됨, 실패시 안된 이유 추가
                if (email.getText().toString().contentEquals("") || password.getText().toString().contentEquals("")
                        || name.getText().toString().contentEquals("") || phone.getText().toString().contentEquals("")) {
                    Toast.makeText(getActivity(), "양식을 다 채워주세요", Toast.LENGTH_SHORT).show();

                    return;
                }

                Toast.makeText(getContext(), "회원가입 처리중...", Toast.LENGTH_SHORT).show();

                // 서버에 email과 password 등록
                FirebaseAuth.getInstance()
                        .createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                final String uid = task.getResult().getUser().getUid();

                                userModel.setUid(uid);
                                userModel.setName(name.getText().toString());
                                userModel.setPhone(phone.getText().toString());
                                userModel.setIdentifier(2); // 배달원일 경우 구분자 2

                                // DB에 배달원 데이터 저장
                                FirebaseDatabase.getInstance().getReference().child("users").child(uid).setValue(userModel)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(getContext(), "회원가입 성공!", Toast.LENGTH_SHORT).show();
                                                getActivity().finish(); // 액티비티 종료
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

    // showDialog : 회사 선택 다이얼로그 생성 후 실행
    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_choice_company, null);
        builder.setView(view);

        final AlertDialog dialog = builder.create();
        dialog.setTitle("택배 회사 선택");

        // 회사 로고 클릭 시 발생하는 이벤트
        view.findViewById(R.id.dialog_choice_company_cjlogistics).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userModel.setCompany("cjlogistics");
                dialog.dismiss();
            }
        });
        view.findViewById(R.id.dialog_choice_company_logen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userModel.setCompany("logen");
                dialog.dismiss();

            }
        });
        view.findViewById(R.id.dialog_choice_company_lotte).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userModel.setCompany("lotte");
                dialog.dismiss();

            }
        });
        view.findViewById(R.id.dialog_choice_company_hanjin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userModel.setCompany("hanjin");
                dialog.dismiss();

            }
        });
        view.findViewById(R.id.dialog_choice_company_epost).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userModel.setCompany("epost");
                dialog.dismiss();

            }
        });

        dialog.show();
    }

}
