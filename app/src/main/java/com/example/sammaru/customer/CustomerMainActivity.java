package com.example.sammaru.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.sammaru.R;
import com.example.sammaru.SettingFragment;
import com.example.sammaru.model.UserModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * 고객 메인 액티비티
 * 이 액티비티는 프래그먼트 3개를 포함하고 있다.
 * 1. 상품 목록 프래그먼트, 2. 배송 조회 프래그먼트, 3. 설정 프래그먼트
 *
 *
 */

//
public class CustomerMainActivity extends AppCompatActivity {

    Fragment deliveryListFragment;
    Fragment lookUpFragment;
    Fragment settingFragment;

    private String company;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_main);

        deliveryListFragment = new DeliveryListFragment();
        lookUpFragment = new LookUpFragment();
        settingFragment = new SettingFragment();

        // 네비게이션 뷰 클릭 시 프래그먼트 이동 이벤트
        BottomNavigationView navView = findViewById(R.id.customer_main_activity_navigation);
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment = null;
                switch (menuItem.getItemId()) {
                    case R.id.action_delivery_list:
                        fragment = deliveryListFragment;
                        break;
                    case R.id.action_lookup:
                        fragment = lookUpFragment;
                        break;
                    case R.id.action_settings:
                        fragment = settingFragment;
                        break;
                }
                return loadFragment(fragment);
            }
        });
        getSupportFragmentManager().beginTransaction().add(R.id.customer_main_activity_framelayout, deliveryListFragment).commit();
    }

    // loadFragment : 네비게이션 뷰 클릭 시 해당하는 프래그먼트를 불러옴
    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.customer_main_activity_framelayout, fragment).commit();
            return true;
        }
        return false;
    }

    public String getCompany() {return company;}

}
