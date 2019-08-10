package com.example.sammaru;


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

import com.example.sammaru.R;

/**
 * 배송 조회 프래그먼트
 * 송장번호를 입력하면 배송 위치를 알려준다 (인터넷으로 연결 또는 직접 레이아웃 구현?)
 *
 */

public class LookUpFragment extends Fragment {

    String delivery_company = null;
    String getDelivery_normalset = "https://tracker.delivery/#/";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_look_up, container, false);

        Spinner spinner = rootView.findViewById(R.id.fragment_lookup_spinner);

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

        final EditText editText = rootView.findViewById(R.id.fragment_lookup_editText);
        Button button = rootView.findViewById(R.id.fragment_lookup_button);

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Uri gotoDelivery = Uri.parse(getDelivery_normalset+delivery_company + editText.getText());
                Intent goDelivery = new Intent(Intent.ACTION_VIEW, gotoDelivery);
                startActivity(goDelivery);
            }
        });
        return rootView;
    }

}
