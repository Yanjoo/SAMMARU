package com.example.sammaru.customer;


import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * 상품 목록 프래그먼트
 * 고객이 주문한 상품들을 리스트 형식으로 보여준다.
 * 고객이 최근에 주문한 상품이 위쪽으로 오도록 정렬해야 한다.
 * 상품 클릭 시 배송하는 기사님과 대화방으로 연결이 되야한다.
 */

public class DeliveryListFragment extends Fragment {

    private FloatingActionButton fab;           // 플로팅 버튼
    private String deliveryCompany = null;      // 택배 회사 이름
    private String baseUrl = "https://tracker.delivery/#/";     // 배송 조회 기본 URL

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_delivery_list, container, false);
        fab = rootView.findViewById(R.id.fragment_delivery_list_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogShow();
            }
        });
        return rootView;
    }

    // dialogShow : 플로팅 버튼 클릭 시 송장번호 등록 다이얼로그 생성
    /**
    * 상품명 직접 입력 받게 수정
    */
    private void dialogShow() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_look_up_add, null);
        builder.setView(view);

        Spinner spinner = view.findViewById(R.id.dialog_lookup_add_spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                switch (position){
                    case 0: deliveryCompany ="de.dhl/"; break;
                    case 1: deliveryCompany ="kr.chunilps/"; break;
                    case 2: deliveryCompany ="kr.cjlogistics/"; break;
                    case 3: deliveryCompany ="kr.cupost/"; break;
                    case 4: deliveryCompany ="kr.cvsnet/"; break;
                    case 5: deliveryCompany ="kr.cway/"; break;
                    case 6: deliveryCompany ="kr.daesin/"; break;
                    case 7: deliveryCompany ="kr.epost/"; break;
                    case 8: deliveryCompany ="kr.hanips/"; break;
                    case 9: deliveryCompany ="kr.hanjin/"; break;
                    case 10: deliveryCompany ="kr.hdexp/"; break;
                    case 11: deliveryCompany ="kr.homepick/"; break;
                    case 12: deliveryCompany ="kr.honamlogis/"; break;
                    case 13: deliveryCompany ="kr.ilyanglogis/"; break;
                    case 14: deliveryCompany ="kr.kdexp/"; break;
                    case 15: deliveryCompany ="kr.kunyoung/"; break;
                    case 16: deliveryCompany ="kr.logen/"; break;
                    case 17: deliveryCompany ="kr.lotte/"; break;
                    case 18: deliveryCompany ="kr.slx/"; break;
                    case 19: deliveryCompany ="nl.tnt/"; break;
                    case 20: deliveryCompany ="un.upu.ems/"; break;
                    case 21: deliveryCompany ="us.fedex/"; break;
                    case 22: deliveryCompany ="us.ups/"; break;
                    case 23: deliveryCompany ="us.usps/"; break;
                    default: break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        final EditText number = view.findViewById(R.id.dialog_lookup_add_editText);

        final AlertDialog dialog = builder.create();
        dialog.setTitle("택배 추가");

        Button add = view.findViewById(R.id.dialog_lookup_add_button_add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 서버에 저장할 데이터
                ProductModel productModel = new ProductModel();
                productModel.setUrl(baseUrl + deliveryCompany + number.getText().toString());

                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                productModel.setUid(uid);

                int startIndex = deliveryCompany.indexOf('.') + 1;
                int endIndex = deliveryCompany.indexOf('/');
                String companyName = deliveryCompany.substring(startIndex, endIndex);

                FirebaseDatabase.getInstance().getReference().child("products").child(companyName).child(uid).push().setValue(productModel)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(getContext(), "등록 성공!", Toast.LENGTH_SHORT).show();
                            }
                        });

                dialog.dismiss();
            }
        });

        Button cancel = view.findViewById(R.id.dialog_lookup_add_button_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

}
