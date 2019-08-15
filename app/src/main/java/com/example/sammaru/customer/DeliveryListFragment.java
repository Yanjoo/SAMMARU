package com.example.sammaru.customer;


import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import com.example.sammaru.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


/**
 * 상품 목록 프래그먼트
 * 고객이 주문한 상품들을 리스트 형식으로 보여준다.
 * 고객이 최근에 주문한 상품이 위쪽으로 오도록 정렬해야 한다.
 * 상품 클릭 시 배송하는 기사님과 대화방으로 연결이 되야한다.
 */
public class DeliveryListFragment extends Fragment {

    private FloatingActionButton fab;
    private String delivery_company = null;
    private String getDelivery_normalset = "https://tracker.delivery/#/";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_delivery_list, container, false);

        fab = rootView.findViewById(R.id.fragment_delivery_list_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogShow();
            }
        });
        return rootView;
    }

    private void dialogShow() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_look_up_add, null);
        builder.setView(view);

        Spinner spinner = view.findViewById(R.id.dialog_lookup_add_spinner);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                switch (position){
                    case 0: delivery_company ="de.dhl/"; break;
                    case 1: delivery_company ="kr.chunilps/"; break;
                    case 2: delivery_company ="kr.cjlogistics/"; break;
                    case 3: delivery_company ="kr.cupost/"; break;
                    case 4: delivery_company ="kr.cvsnet/"; break;
                    case 5: delivery_company ="kr.cway/"; break;
                    case 6: delivery_company ="kr.daesin/"; break;
                    case 7: delivery_company ="kr.epost/"; break;
                    case 8: delivery_company ="kr.hanips/"; break;
                    case 9: delivery_company ="kr.hanjin/"; break;
                    case 10: delivery_company ="kr.hdexp/"; break;
                    case 11: delivery_company ="kr.homepick/"; break;
                    case 12: delivery_company ="kr.honamlogis/"; break;
                    case 13: delivery_company ="kr.ilyanglogis/"; break;
                    case 14: delivery_company ="kr.kdexp/"; break;
                    case 15: delivery_company ="kr.kunyoung/"; break;
                    case 16: delivery_company ="kr.logen/"; break;
                    case 17: delivery_company ="kr.lotte/"; break;
                    case 18: delivery_company ="kr.slx/"; break;
                    case 19: delivery_company ="nl.tnt/"; break;
                    case 20: delivery_company ="un.upu.ems/"; break;
                    case 21: delivery_company ="us.fedex/"; break;
                    case 22: delivery_company ="us.ups/"; break;
                    case 23: delivery_company ="us.usps/"; break;
                    default: break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        final AlertDialog dialog = builder.create();
        dialog.setTitle("택배 추가");

        Button cancel = view.findViewById(R.id.dialog_lookup_add_button_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();


    }

}
