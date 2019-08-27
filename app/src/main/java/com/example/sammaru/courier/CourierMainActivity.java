package com.example.sammaru.courier;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.sammaru.R;
import com.example.sammaru.SettingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

/**
 * 기사 메인 액티비티
 * 기능 : 이 액티비티는 3개의 프래그먼트를 다룬다
 * 1. 고객 목록 프래그먼트 2. 3. 설정 프래그먼트
 *
 */

public class CourierMainActivity extends AppCompatActivity {

    private Fragment customerListFragment;
    private Fragment settingFragment;
    private Fragment productListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courier_main);

        customerListFragment = new CustomerListFragment();
        settingFragment = new SettingFragment();
        productListFragment = new ProductListCourierFragment();

        // 네비게이션 뷰 클릭 이벤트
        BottomNavigationView navView = findViewById(R.id.courier_main_activity_navigation);
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment = null;
                switch (menuItem.getItemId()) {
                    case R.id.action_customer_list:
                        fragment = customerListFragment;
                        break;
                    case R.id.action_products_list:
                        fragment = productListFragment;
                        break;
                    case R.id.action_settings:
                        fragment = settingFragment;
                        break;
                }
                return loadFragment(fragment);
            }
        });
        getSupportFragmentManager().beginTransaction().add(R.id.courier_main_activity_framelayout, new CustomerListFragment()).commit();

        passPushTokenToServer();
    }

    // loadFragment : 네비게이션 뷰 클릭시 해당하는 프래그먼트 로딩
    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.courier_main_activity_framelayout, fragment).commit();
            return true;
        }
        return false;
    }

    void passPushTokenToServer() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String token = FirebaseInstanceId.getInstance().getToken();

        Map<String, Object> map = new HashMap<>();
        map.put("pushToken", token);

        FirebaseDatabase.getInstance().getReference().child("users").child(uid).updateChildren(map);
    }
}
