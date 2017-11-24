package com.user.maevis;

/**
 * Created by Chen on 10/28/2017.
 */

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class Tab1_Home extends Fragment {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1_home, container, false);
//        FloatingActionButton btnAddReport = (FloatingActionButton) rootView.findViewById(R.id.fab);
//        btnAddReport.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v){
//                Intent i = new Intent(getActivity(), SelectionPage.class);
//                startActivity(i);
//            }
//        });



        return rootView;

    }


}
