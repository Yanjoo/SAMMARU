package com.example.sammaru;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.sammaru.fragment.CustomerListFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DeliveryMainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_main);

        BottomNavigationView navView = findViewById(R.id.delivery_main_activity_navigation);
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                Fragment fragment = null;

                switch (menuItem.getItemId()) {
                    case R.id.action_customer_list:
                        fragment = new CustomerListFragment();
                        break;

                    case R.id.action_settings:
                        fragment = new SettingFragment();
                        break;
                }

                return loadFragment(fragment);
            }
        });

        getSupportFragmentManager().beginTransaction().add(R.id.delivery_main_activity_framelayout, new CustomerListFragment()).commit();

    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.delivery_main_activity_framelayout, fragment).commit();
            return true;
        }
        return false;
    }


}
