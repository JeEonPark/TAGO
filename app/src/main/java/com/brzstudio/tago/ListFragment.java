package com.brzstudio.tago;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class ListFragment extends Fragment implements View.OnClickListener {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        Button signoutButton = (Button) view.findViewById(R.id.signoutButton);

        signoutButton.setOnClickListener(this);

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