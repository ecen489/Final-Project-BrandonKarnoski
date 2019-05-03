package com.example.hungry;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapsActivity2 extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public String placeName;
    public double myLat, myLng;    //These are the device locations
    public double mLat, mLng;   //These are the destinations
    public String mLatS, mLngS;
    public String title = "Destination: ";
    public String arrival = "Arrived at: ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);
        //Grab the extras
        Intent placeIntent = getIntent();
        placeName = placeIntent.getStringExtra("placeName");
        myLat = placeIntent.getDoubleExtra("myLat", 0);
        myLng = placeIntent.getDoubleExtra("myLng", 0);
        mLatS = placeIntent.getStringExtra("mLat");
        mLngS = placeIntent.getStringExtra("mLng");
        mLat = Double.parseDouble(mLatS);
        mLng = Double.parseDouble(mLngS);




        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapAct2);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();
        mMap = googleMap;
        LatLng deviceLoc = new LatLng(myLat, myLng);
        LatLng placeLoc = new LatLng(mLat, mLng);
        setMyMap(deviceLoc,placeLoc , "Current Location", placeName);
        Notification.Builder builder = new Notification.Builder(MapsActivity2.this)
                .setContentTitle(title)
                .setContentText(placeName)
                .setSmallIcon(R.drawable.food)
                .setAutoCancel(true);
        Notification notification = builder.build();
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1, notification);
    }
    public void setMyMap(LatLng myLatLng, LatLng mLatLng, String name, String destName){
        mMap.addMarker(new MarkerOptions().position(myLatLng).title(name));
        Marker placeMarker = mMap.addMarker(new MarkerOptions().position(mLatLng).title(destName));
        placeMarker.showInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLatLng, 13));

    }
    public void currentLocation(){
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(MapsActivity2.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MapsActivity2.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }
        else{

            Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(loc != null){
                myLat = loc.getLatitude();
                myLng = loc.getLongitude();
                LatLng locn = new LatLng(myLat, myLng);
                Toast.makeText(MapsActivity2.this, "Got Location", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void onArrival(View view){
        Notification.Builder builder = new Notification.Builder(MapsActivity2.this)
                .setContentTitle(arrival)
                .setContentText(placeName)
                .setSmallIcon(R.drawable.food)
                .setAutoCancel(true);
        Notification notification = builder.build();
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(2, notification);
    }


}
