package com.example.sammaru.customer;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sammaru.R;

/**
 * 배송 조회 프래그먼트
 * 송장번호를 입력하면 배송 위치를 알려준다 (인터넷으로 연결 또는 직접 레이아웃 구현?)
 *
 */

public class LookUpFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_look_up, container, false);
    }

}
