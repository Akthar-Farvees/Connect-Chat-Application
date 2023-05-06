package com.connect.connectproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.connect.connectproject.Fragments.MapFragment;

public class    MapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Fragment fragment = new MapFragment();

        //Get the map functionality from map fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_map,fragment).commit();

        //Hide the tab bar
        getSupportActionBar().hide();
    }
}