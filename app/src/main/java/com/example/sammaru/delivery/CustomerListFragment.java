package com.example.sammaru.delivery;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sammaru.R;

/**
 * 고객 목록 프래그먼트
 * 배송해야하는 고객들을 리스트 형식으로 보여준다.
 * 고객 클릭 시 고객과의 대화 창으로 넘어가야한다.
 */

public class CustomerListFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_customer_list, container, false);
    }

}
