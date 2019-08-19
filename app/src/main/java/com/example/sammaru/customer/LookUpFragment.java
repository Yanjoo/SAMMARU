package com.example.sammaru.customer;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.service.autofill.Dataset;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
 * 배송 조회 프래그먼트
 * 송장번호를 입력하면 배송 위치를 알려준다 (인터넷으로 연결 또는 직접 레이아웃 구현?)
 *
 */

public class LookUpFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_look_up, container, false);

        RecyclerView recyclerView = rootView.findViewById(R.id.fragment_lookup_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        recyclerView.setAdapter(new LookUpFragmentRecyclerViewAdapter());

        return rootView;
    }

    class LookUpFragmentRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        List<ProductModel> productModels;

        public LookUpFragmentRecyclerViewAdapter() {
            productModels = new ArrayList<>();
            final String myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            Log.d("LookUpFragment", myUid);

            FirebaseDatabase.getInstance().getReference().child("products").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    productModels.clear();

                    for (DataSnapshot company : dataSnapshot.getChildren()) {
                        for (DataSnapshot id : company.getChildren()) {
                            for (DataSnapshot item : id.getChildren()) {
                                ProductModel productModel = item.getValue(ProductModel.class);
                                Log.d("LookUpFragment", item.toString());
                                if (productModel.getUid().equals(myUid)) {
                                    productModels.add(productModel);
                                }
                            }
                            notifyDataSetChanged();
                        }
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
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
        }

        @Override
        public int getItemCount() {
            return productModels.size();
        }

        private class CustomeViewHolder extends RecyclerView.ViewHolder {

            public CustomeViewHolder(@NonNull View itemView) {
                super(itemView);
            }
        }
    }

}
