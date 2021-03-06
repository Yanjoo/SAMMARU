package com.cbnu.sammaru.courier;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.cbnu.sammaru.R;
import com.cbnu.sammaru.model.ProductModel;
import com.cbnu.sammaru.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProductListCourierFragment extends Fragment {

    private String companyName;
    private String uid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.fragment_product_list_courier, container, false);

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseDatabase.getInstance().getReference().child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot users : dataSnapshot.getChildren()) {
                    UserModel userModel = users.getValue(UserModel.class);
                    if (userModel.getUid().equals(uid)) {
                        companyName = userModel.getCompany();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        // 리사이클러뷰로 회사에 맞는 아이템을 리스트로 보여줌
        RecyclerView recyclerView = rootView.findViewById(R.id.fragment_product_list_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        recyclerView.setAdapter(new ProductListFragmentAdapter());

        return rootView;
    }

    class ProductListFragmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        List<ProductModel> products;

        public ProductListFragmentAdapter() {
            products = new ArrayList<>();

            FirebaseDatabase.getInstance().getReference().child("products").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    products.clear();

                    for (DataSnapshot company : dataSnapshot.getChildren()) {
                        if (company.getKey().equals(companyName)) {
                            for (DataSnapshot item : company.getChildren()) {
                                ProductModel productModel = item.getValue(ProductModel.class);
                                products.add(productModel);
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
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_product_courier, parent, false);
            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
            ((CustomViewHolder)holder).address.setText(products.get(position).getAddress());
            ((CustomViewHolder)holder).number.setText(products.get(position).getInvoiceNumber());
            ((CustomViewHolder)holder).addBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ProductModel product = products.get(position);
                    product.setCourierUid(uid);
                    FirebaseDatabase.getInstance().getReference().child("register").child("products").push().setValue(product).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });
                }
            });
        }

        @Override
        public int getItemCount() {
            return products.size();
        }

        private class CustomViewHolder extends RecyclerView.ViewHolder {
            public TextView address;
            public TextView number;
            public Button addBtn;

            public CustomViewHolder(@NonNull View itemView) {
                super(itemView);

                address = itemView.findViewById(R.id.item_product_courier_address);
                number = itemView.findViewById(R.id.item_product_courier_number);
                addBtn = itemView.findViewById(R.id.item_product_courier_button_add);
            }
        }

    }

}
