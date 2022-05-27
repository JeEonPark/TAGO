package com.brzstudio.tago;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.firebase.auth.FirebaseAuth;

import net.daum.mf.map.api.MapView;


public class FindFragment extends Fragment implements View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_find, container, false);

        //화면 크기에 맞게 지도 크기 변경
        setMapSize(view);
        MapView mapView = new MapView(getActivity());

        ViewGroup mapViewContainer = (ViewGroup) view.findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);

        return view;
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signoutButton:
                FirebaseAuth.getInstance().signOut();
                FragmentActivity f = getActivity();
                Intent intent = new Intent(f, LoginActivity.class);
                startActivity(intent);
                f.finish();

        }
    }

    public void setMapSize(View view) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int widthPixels = metrics.widthPixels;
        int heightPixels = metrics.heightPixels - dpToPx(340);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(widthPixels, heightPixels);

        view.findViewById(R.id.map_view).setLayoutParams(params);

    }


    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public int pxToDp(int px) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

}