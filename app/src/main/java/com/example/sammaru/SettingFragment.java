package com.example.sammaru;

        import android.content.Intent;
        import android.os.Bundle;

        import androidx.fragment.app.Fragment;

        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;

        import com.example.sammaru.courier.CourierMainActivity;
        import com.example.sammaru.login.LoginActivity;

/**
 * 이 프래그먼트는 설정 프래그먼트이다
 * 알림 설정, 버전 정보, 매크로 설정, 택배사 정보, 광고 제거 등 넣어야 한다.
 */

public class SettingFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_setting, container, false);

        Button logout_button = rootView.findViewById(R.id.fragment_setting_logout_button);
        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            }
        });

        return rootView;
    }

}
