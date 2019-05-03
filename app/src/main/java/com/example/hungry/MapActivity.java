package com.example.hungry;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

     private GoogleMap mMap;
     public String placeName;
    public double myLat, myLng;    //These are the device locations
    public double mLat, mLng;   //These are the destinations
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        //Grab the extras
        Intent placeIntent = getIntent();
        placeName = placeIntent.getStringExtra("placeName");
        myLat = placeIntent.getDoubleExtra("myLat", 0);
        myLng = placeIntent.getDoubleExtra("myLng", 0);
        mLat = placeIntent.getDoubleExtra("mLat", 0);
        mLng = placeIntent.getDoubleExtra("mLng", 0);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapActivity.this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap){
        Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();
        mMap = googleMap;
        LatLng deviceLoc = new LatLng(myLat, myLng);
        LatLng placeLoc = new LatLng(mLat, mLng);
        setMyMap(deviceLoc, "Current Location");
    }


    public void setMyMap(LatLng myLatLng, String name){
        mMap.addMarker(new MarkerOptions().position(myLatLng).title(name));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(myLatLng));
    }
}
