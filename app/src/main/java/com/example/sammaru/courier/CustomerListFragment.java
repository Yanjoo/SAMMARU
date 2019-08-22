package com.example.sammaru.courier;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sammaru.R;
import com.example.sammaru.model.ProductModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 고객 목록 프래그먼트
 * 배송해야하는 고객들을 리스트 형식으로 보여준다.
 * 고객 클릭 시 고객과의 대화 창으로 넘어가야한다.
 */

public class CustomerListFragment extends Fragment {

    private String uid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.fragment_customer_list, container, false);

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        RecyclerView recyclerView = rootView.findViewById(R.id.fragment_customer_list_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        recyclerView.setAdapter(new CustomerListFragmentAdapter());

        return rootView;
    }

    private class CustomerListFragmentAdapter extends RecyclerView.Adapter {

        List<ProductModel> products;

        CustomerListFragmentAdapter() {
            products = new ArrayList<>();

            FirebaseDatabase.getInstance().getReference().child("register").child("products").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    products.clear();
                    for (DataSnapshot item : dataSnapshot.getChildren()) {
                        ProductModel productModel = item.getValue(ProductModel.class);
                        if (productModel.getCourierUid().equals(uid)) {
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
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_customer_info, parent, false);
            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getActivity(), "채팅방 연결 필요", Toast.LENGTH_SHORT).show();
                }
            });

            ((CustomViewHolder)holder).address.setText("고객 주소");
            ((CustomViewHolder)holder).number.setText("송장 번호 : " + products.get(position).getNumber());
            ((CustomViewHolder)holder).bell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getActivity(), "배송 출발 알람 송신", Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return products.size();
        }

        private class CustomViewHolder extends RecyclerView.ViewHolder {

            private TextView address;
            private TextView number;
            private ImageView bell;

            public CustomViewHolder(@NonNull View itemView) {
                super(itemView);

                address = itemView.findViewById(R.id.item_customer_info_address);
                number = itemView.findViewById(R.id.item_customer_info_number);
                bell = itemView.findViewById(R.id.item_customer_info_bell);
            }
        }
    }
}
