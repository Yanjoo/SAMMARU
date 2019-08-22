package com.example.sammaru.customer;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
 * 배송 조회 프래그먼트
 *
 * 기능 : 서버에서 유저에 해당하는 상품 들을 리스트로 보여줌, 클릭시 배송 조회 인터넷으로 연결
 *
 * 수정 사항 : 아이템에 상품명 보이게 수정
 */

public class LookUpFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_look_up, container, false);

        // 리사이클러뷰로 아이템을 리스트로 보여줌
        RecyclerView recyclerView = rootView.findViewById(R.id.fragment_lookup_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        recyclerView.setAdapter(new LookUpFragmentRecyclerViewAdapter());

        return rootView;
    }

    class LookUpFragmentRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        List<ProductModel> productModels;  // 서버에서 유저에 해당하는 배송상품을 담을 List

        public LookUpFragmentRecyclerViewAdapter() {
            productModels = new ArrayList<>();

            final String myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

            // DB에서 유저에 해당하는 아이템들을 productModels에 저장
            FirebaseDatabase.getInstance().getReference().child("products").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    productModels.clear();

                    for (DataSnapshot company : dataSnapshot.getChildren()) {
                        for (DataSnapshot item : company.getChildren()) {
                            ProductModel productModel = item.getValue(ProductModel.class);
                            if (productModel.getUid().equals(myUid)) {
                                productModels.add(productModel);
                            }
                        }
                        notifyDataSetChanged();

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_customer, parent, false);
            return new CustomeViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ProductModel productModel = productModels.get(position);
                    Uri gotoDelivery = Uri.parse(productModel.getUrl());
                    Intent goDelivery = new Intent(Intent.ACTION_VIEW, gotoDelivery);
                    startActivity(goDelivery);
                }
            });

            ((CustomeViewHolder)holder).itemName.setText(productModels.get(position).getProductName());
        }

        @Override
        public int getItemCount() {
            return productModels.size();
        }

        private class CustomeViewHolder extends RecyclerView.ViewHolder {
            public TextView itemName;

            public CustomeViewHolder(@NonNull View itemView) {
                super(itemView);
                itemName = itemView.findViewById(R.id.item_product_name);
            }
        }
    }

}
