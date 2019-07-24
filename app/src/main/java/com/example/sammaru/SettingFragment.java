package com.example.sammaru;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 이 프래그먼트는 설정 프래그먼트이다
 * 알림 설정, 버전 정보, 매크로 설정, 택배사 정보, 광고 제거 등 넣어야 한다.
 */

public class SettingFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

}
