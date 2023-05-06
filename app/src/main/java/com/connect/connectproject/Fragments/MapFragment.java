package com.connect.connectproject.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;


import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.connect.connectproject.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragment extends Fragment implements LocationListener {

    private GoogleMap googleMap;
    private LocationManager locationManager;
    private Marker currentLocationMarker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map, container, false);
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.MY_MAP);
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                MapFragment.this.googleMap = googleMap;

//                //Set the initial location
//                LatLng connect = new LatLng(7.471155387945517, 81.86530735202396);
//                googleMap.addMarker(new MarkerOptions().position(connect).title("Connect Company"));
//                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(connect, 18.0f));
                googleMap.getUiSettings().setZoomControlsEnabled(true);

                // Check for location permission
                if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    // Get user's current location
                    locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
                    if (locationManager != null) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, MapFragment.this);
                    }
                } else {
                    // Request location permission if not granted
                    ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
                }
            }
        });

        return view;
    }

    @Override
    public void onLocationChanged( Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        if (currentLocationMarker != null) {
            currentLocationMarker.setPosition(latLng);
        } else {
            currentLocationMarker = googleMap.addMarker(new MarkerOptions().position(latLng).title("Current Location"));
        }
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18.0f));
    }

    // Other LocationListener interface methods
    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled( String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }
}
