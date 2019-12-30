package com.cbnu.sammaru.courier;

import android.app.ActivityOptions;
import android.content.Intent;
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

import com.cbnu.sammaru.R;
import com.cbnu.sammaru.chat.MessageActivity;
import com.cbnu.sammaru.model.NotificationModel;
import com.cbnu.sammaru.model.ProductModel;
import com.cbnu.sammaru.model.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 고객 목록 프래그먼트
 * 배송해야하는 고객들을 리스트 형식으로 보여준다.
 * 고객 클릭 시 고객과의 대화 창으로 넘어가야한다.
 */

public class CustomerListFragment extends Fragment {

    private String uid;
    private UserModel destinationUserModel;

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

    private void sendFcm(String productName) {
        Gson gson = new Gson();

        String userName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();

        NotificationModel notificationModel = new NotificationModel();
        notificationModel.to = destinationUserModel.pushToken;
        notificationModel.notification.title = userName;
        notificationModel.notification.text = productName + " 상품이 발송 중입니다.";
        notificationModel.data.title = userName;
        notificationModel.data.text = productName + " 상품이 발송 중입니다.";
        notificationModel.data.id = String.valueOf(destinationUserModel.getIdentifier());

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf8"), gson.toJson(notificationModel));

        Request request = new Request.Builder()
                .header("Content-Type", "application/json")
                .addHeader("Authorization", "key=AAAAUIpBwks:APA91bFyB0l4lo8_3fxZA-srAudzr3g6Kgyj2cQbJsIQgs9xIx2P" +
                        "fCoGi3kxK1Hv321a5F2heFURks8zJZkCVEBa3sAXi3XmtG-9KkZJOoBzJ_ptanLE35J9sAz4pO5OpwzGTdrflNNj")
                .url("https://fcm.googleapis.com/fcm/send")
                .post(requestBody)
                .build();

        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });

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
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), MessageActivity.class);
                    intent.putExtra("destinationUid", products.get(position).getCustomerUid());
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    ActivityOptions activityOptions = ActivityOptions.makeCustomAnimation(view.getContext(), R.anim.fromright, R.anim.toleft);
                    startActivity(intent, activityOptions.toBundle());

                }
            });

            ((CustomViewHolder)holder).address.setText(products.get(position).getAddress());
            ((CustomViewHolder)holder).number.setText("송장 번호 : " + products.get(position).getInvoiceNumber());
            ((CustomViewHolder)holder).name.setText(products.get(position).getProductName());
            ((CustomViewHolder)holder).bell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseDatabase.getInstance().getReference().child("users").child(products.get(position).getCustomerUid())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            destinationUserModel = dataSnapshot.getValue(UserModel.class);
                            sendFcm(products.get(position).getProductName());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

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
            public TextView name;
            public ImageView bell;

            public CustomViewHolder(@NonNull View itemView) {
                super(itemView);

                address = itemView.findViewById(R.id.item_customer_info_address);
                number = itemView.findViewById(R.id.item_customer_info_number);
                bell = itemView.findViewById(R.id.item_customer_info_bell);
                name = itemView.findViewById(R.id.item_customer_info_name);
            }
        }
    }
}
