package com.example.sammaru.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.sammaru.R;
import com.example.sammaru.SettingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * 고객 메인 액티비티
 * 이 액티비티는 프래그먼트 3개를 포함하고 있다.
 * 1. 상품 목록 프래그먼트, 2. 배송 조회 프래그먼트, 3. 설정 프래그먼트
 *
 *
 */

public class CustomerMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_main);

        // 네비게이션 뷰 클릭 시 프래그먼트 이동 이벤트
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

    // 네비게이션 뷰 클릭 시 해당하는 프래그먼트를 불러옴
    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.customer_main_activity_framelayout, fragment).commit();
            return true;
        }
        return false;
    }

}
