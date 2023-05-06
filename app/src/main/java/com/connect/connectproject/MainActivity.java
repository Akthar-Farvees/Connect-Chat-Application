package com.connect.connectproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;


import com.connect.connectproject.Adapter.FragmentsAdapter;
import com.connect.connectproject.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;

import com.google.android.material.tabs.TabLayout;





public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        binding.viewPager.setAdapter(new FragmentsAdapter(getSupportFragmentManager()));
        binding.tabLayout.setupWithViewPager(binding.viewPager);


//        getSupportActionBar().hide();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.settings:
                Intent intent1 = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent1);
                break;
            case R.id.groupChat:
                Intent intent2 = new Intent(MainActivity.this, GroupChatActivity.class);
                startActivity(intent2);
                break;
            case R.id.about:
                Intent intent3 = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent3);
                break;
            case R.id.logout:
                mAuth.signOut(); //Sign out Functionality
                Intent intent4 = new Intent(MainActivity.this, SignInActivity.class);
                startActivity(intent4);
                break;
            case R.id.location:
                Intent intent5 = new Intent(MainActivity.this, MapActivity.class);
                startActivity(intent5);
                break;
            case R.id.weather:
                Intent intent6 = new Intent(MainActivity.this, WeatherActivity.class);
                startActivity(intent6);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}