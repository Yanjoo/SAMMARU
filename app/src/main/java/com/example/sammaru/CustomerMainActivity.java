package com.example.sammaru;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.sammaru.R;
import com.example.sammaru.fragment.CustomerListFragment;
import com.example.sammaru.fragment.DeliveryListFragment;
import com.example.sammaru.fragment.LookUpFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CustomerMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_main);

        BottomNavigationView navView = findViewById(R.id.customer_main_activity_navigation);
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                Fragment fragment = null;

                switch (menuItem.getItemId()) {
                    case R.id.action_delivery_list:
                        fragment = new DeliveryListFragment();
                        break;

                    case R.id.action_lookup:
                        fragment = new LookUpFragment();
                        break;

                    case R.id.action_settings:
                        fragment = new SettingFragment();
                        break;
                }

                return loadFragment(fragment);
            }
        });

        getSupportFragmentManager().beginTransaction().add(R.id.customer_main_activity_framelayout, new DeliveryListFragment()).commit();

    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.customer_main_activity_framelayout, fragment).commit();
            return true;
        }
        return false;
    }

}
