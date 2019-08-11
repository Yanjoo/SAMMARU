package com.example.sammaru.login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.sammaru.R;

// test
// DooHee
// Hanju
public class JoinActivity extends AppCompatActivity {

    CourierJoinFragment courierJoinFragment;
    CustomerJoinFragment customerJoinFragment;

    ImageView courierImageView;
    ImageView customerImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        courierImageView = findViewById(R.id.activity_join_courierImageView);
        courierImageView.setOnClickListener(new View.OnClickListener() {
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

        courierJoinFragment = new CourierJoinFragment();
        customerJoinFragment = new CustomerJoinFragment();
        onFragmentChanged(0);
    }

    public void onFragmentChanged(int index) {
        switch(index) {
            case 0:
                getSupportFragmentManager().beginTransaction().replace(R.id.activity_join_frame_layout, courierJoinFragment).commit();
                courierImageView.setImageResource(R.drawable.courier);
                customerImageView.setImageResource(R.drawable.user_transparent);
                break;

            case 1:
                getSupportFragmentManager().beginTransaction().replace(R.id.activity_join_frame_layout, customerJoinFragment).commit();
                courierImageView.setImageResource(R.drawable.courier_transparent);
                customerImageView.setImageResource(R.drawable.user);
                break;

                default:
                    break;
        }
    }

}
