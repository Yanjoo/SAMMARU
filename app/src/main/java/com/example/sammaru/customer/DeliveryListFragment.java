package com.example.sammaru.customer;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.sammaru.R;


/**
 * 상품 목록 프래그먼트
 * 고객이 주문한 상품들을 리스트 형식으로 보여준다.
 * 고객이 최근에 주문한 상품이 위쪽으로 오도록 정렬해야 한다.
 * 상품 클릭 시 배송하는 기사님과 대화방으로 연결이 되야한다.
 */
public class DeliveryListFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_delivery_list, container, false);
    }

}
