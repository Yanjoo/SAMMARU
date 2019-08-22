package com.example.sammaru.courier;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sammaru.R;
import com.example.sammaru.model.ProductModel;
import com.example.sammaru.model.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductListFragment extends Fragment {

    private String companyName;
    private String uid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.fragment_product_list, container, false);

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseDatabase.getInstance().getReference().child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot users : dataSnapshot.getChildren()) {
                    UserModel userModel = users.getValue(UserModel.class);
                    if (userModel.getUid().equals(uid)) {
                        companyName = userModel.getCompany();
                        Log.d("ProductListFragment", companyName);
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
                        Log.d("ProductListFragment ", company.getKey());
                        if (company.getKey().equals(companyName)) {
                            for (DataSnapshot item : company.getChildren()) {
                                ProductModel productModel = item.getValue(ProductModel.class);
                                products.add(productModel);
                                Log.d("ProductListFragment", " item add");
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
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_all_product_courier, parent, false);
            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            ((CustomViewHolder)holder).address.setText("고객 주소");
            ((CustomViewHolder)holder).number.setText(products.get(position).getNumber());
            ((CustomViewHolder)holder).addBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getActivity(), "인연이라고 하죠", Toast.LENGTH_SHORT).show();
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

                address = itemView.findViewById(R.id.item_all_product_courier_address);
                number = itemView.findViewById(R.id.item_all_product_courier_number);
                addBtn = itemView.findViewById(R.id.item_all_product_courier_button_add);
            }
        }

    }

}
