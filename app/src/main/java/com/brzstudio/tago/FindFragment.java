package com.brzstudio.tago;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.firebase.auth.FirebaseAuth;

import net.daum.mf.map.api.MapView;


public class FindFragment extends Fragment implements View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_find, container, false);


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

}