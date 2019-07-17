package com.example.sammaru;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.sammaru.fragment.CustomerJoinFragment;
import com.example.sammaru.fragment.DeliveryJoinFragment;

// test
// DooHee
public class JoinActivity extends AppCompatActivity {

    DeliveryJoinFragment deliveryJoinFragment;
    CustomerJoinFragment customerJoinFragment;

    ImageView deliveryImageView;
    ImageView customerImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        deliveryImageView = findViewById(R.id.activity_join_deliveryImageView);
        deliveryImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFragmentChanged(0);
            }
        });

        customerImageView = findViewById(R.id.activity_join_customerImageView);
        customerImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFragmentChanged(1);
            }
        });

        deliveryJoinFragment = new DeliveryJoinFragment();
        customerJoinFragment = new CustomerJoinFragment();
        onFragmentChanged(0);
    }

    public void onFragmentChanged(int index) {
        switch(index) {
            case 0:
                getSupportFragmentManager().beginTransaction().replace(R.id.activity_join_frame_layout, deliveryJoinFragment).commit();
                break;

            case 1:
                getSupportFragmentManager().beginTransaction().replace(R.id.activity_join_frame_layout, customerJoinFragment).commit();
                break;

                default:
                    break;
        }
    }

}
