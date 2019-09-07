package com.example.sammaru.customer;


import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sammaru.R;
import com.example.sammaru.chat.MessageActivity;
import com.example.sammaru.model.ProductModel;
import com.example.sammaru.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * 상품 목록 프래그먼트
 *
 * 고객이 주문한 상품들을 리스트 형식으로 보여준다.
 * 고객이 최근에 주문한 상품이 위쪽으로 오도록 정렬해야 한다.
 * 상품 클릭 시 배송하는 기사님과 대화방으로 연결이 되야한다.
 */

public class ProductListCustomerFragment extends Fragment {

    private FloatingActionButton fab;                       // 플로팅 버튼
    private String deliveryCompany = null;                  // 택배 회사 이름
    private String baseUrl = "https://tracker.delivery/#/"; // 배송 조회 기본 URL

    private String address;
    private String uid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_product_list_customer, container, false);

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // 주소 설정
        FirebaseDatabase.getInstance().getReference().child("users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserModel userModel = dataSnapshot.getValue(UserModel.class);
                address = userModel.getAddress();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // 플로팅 버튼 클릭 이벤트
        fab = rootView.findViewById(R.id.fragment_delivery_list_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogShow();
            }
        });

        RecyclerView recyclerView = rootView.findViewById(R.id.fragment_delivery_list_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        recyclerView.setAdapter(new DeliveryListFragmentAdapter());

        return rootView;
    }

    // dialogShow : 플로팅 버튼 클릭시 dialog_look_up_add 다이얼로그 생성
    private void dialogShow() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_look_up_add, null);
        builder.setView(view);

        // 택배회사 목록을 보여줌
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

        // 송장번호
        final EditText number = view.findViewById(R.id.dialog_lookup_add_editText_number);

        // 아이템 명
        final EditText item = view.findViewById(R.id.dialog_lookup_add_editText_item);

        final AlertDialog dialog = builder.create();
        dialog.setTitle("택배 추가");

        // 추가 버튼 클릭 이벤트
        Button add = view.findViewById(R.id.dialog_lookup_add_button_add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 서버에 저장할 데이터

                // url 설정
                final ProductModel productModel = new ProductModel();
                productModel.setUrl(baseUrl + deliveryCompany + number.getText().toString());

                // 송장번호 설정
                productModel.setNumber(number.getText().toString());

                // uid 설정
                productModel.setCustomerUid(uid);

                // 상품 이름 설정
                String name = item.getText().toString();
                productModel.setProductName(name);



                productModel.setAddress(address);

                // 택배 회사 설정
                int startIndex = deliveryCompany.indexOf('.') + 1;
                int endIndex = deliveryCompany.indexOf('/');
                String companyName = deliveryCompany.substring(startIndex, endIndex);

                FirebaseDatabase.getInstance().getReference().child("products").child(companyName).push().setValue(productModel)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(getContext(), "등록 성공!", Toast.LENGTH_SHORT).show();
                            }
                        });

                dialog.dismiss();
            }
        });

        // 취소 버튼 클릭 이벤트
        Button cancel = view.findViewById(R.id.dialog_lookup_add_button_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private class DeliveryListFragmentAdapter extends RecyclerView.Adapter {

        List<ProductModel> products;

        DeliveryListFragmentAdapter() {
            products = new ArrayList<>();

            FirebaseDatabase.getInstance().getReference().child("register").child("products").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    products.clear();
                    for (DataSnapshot item : dataSnapshot.getChildren()) {
                        ProductModel productModel = item.getValue(ProductModel.class);
                        if (productModel.getCustomerUid().equals(uid)) {
                            products.add(productModel);
                        }
                    }
                    notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_delivery_info, parent, false);
            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), MessageActivity.class);
                    intent.putExtra("destinationUid", products.get(position).getCourierUid());
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    ActivityOptions activityOptions = ActivityOptions.makeCustomAnimation(view.getContext(), R.anim.fromright, R.anim.toleft);
                    startActivity(intent, activityOptions.toBundle());
                }
            });

            ((CustomViewHolder)holder).productName.setText(products.get(position).getProductName());
            ((CustomViewHolder)holder).number.setText(products.get(position).getNumber());
            ((CustomViewHolder)holder).btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getActivity(), "파이어베이스에서 삭제 해야됨", Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return products.size();
        }

        private class CustomViewHolder extends RecyclerView.ViewHolder {
            TextView productName;
            TextView number;
            Button btnDelete;

            public CustomViewHolder(@NonNull View itemView) {
                super(itemView);

                productName = itemView.findViewById(R.id.item_delivery_info_name);
                number = itemView.findViewById(R.id.item_delivery_info_number);
                btnDelete = itemView.findViewById(R.id.item_delivery_info_btn);
            }
        }
    }
}
