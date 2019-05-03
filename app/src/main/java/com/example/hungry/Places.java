package com.example.hungry;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.LogPrinter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class Places extends AppCompatActivity {
    //Initialize variables
    public double myLat = 30.62;    //These are the device locations
    public double myLng = -96.34;   //These are the device locations
    public String mLat = "0";
    public String mLng = "0";   //These are the destinations
    int radius = 8046;
    String indexName = "";
    ListView mList;
    FloatingActionButton fab;

    ArrayAdapter myAdapter;
    final ArrayList<Restaurant> restHolder = new ArrayList<>();

    ArrayList<String> myPlaces = new ArrayList<>();


    //Firebase
    FirebaseDatabase mFire;
    DatabaseReference mBase;
    FirebaseAuth mAuth;



    //API Code
    private static final String API_KEY ="@strings/google_maps_key";
    private static final String PLACES_API = "https://maps.googleapis.com/maps/api/place";

    private static final String TAG = "Places List attempt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);
        //Firebase
        mFire = FirebaseDatabase.getInstance();
        mBase = mFire.getReference("restaurants");



        mList = findViewById(R.id.listView);
        myAdapter = new ArrayAdapter<String>(Places.this, android.R.layout.simple_list_item_1, myPlaces);
        mList.setAdapter(myAdapter);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String entry = parent.getItemAtPosition(position).toString();
                int entryVal = position;
                //indexName = myPlaces.get(entryVal);
                //Toast.makeText(getApplicationContext(),indexName,Toast.LENGTH_LONG).show();
                String mSelected = myPlaces.get(entryVal);
                Log.i("This is position", Integer.toString(position));
                getLocation(mSelected, entryVal);
                indexName = mSelected;
            }
        });
        String name = "";
        String latty = "";
        String longy = "";
        final Restaurant mRest = new Restaurant(name, latty, longy);

        mBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot data) {
                Log.i("**********","onDataChange" );

                for (DataSnapshot ds : data.getChildren()) {
                    Log.i("**********","first for loop" );
                    for(DataSnapshot snap : ds.getChildren()) {
                        //Log.i("***********", snap.getKey());
                        if (snap.getKey().equals("id")) {
                            String ID = snap.getValue(String.class);
                            mRest.setName(ID);
                            myPlaces.add(ID);
                            Log.i("**********", ID);
                        } else if (snap.getKey().equals("latitude")) {
                            mRest.setLongitude(snap.getValue(String.class));
                            Log.i("***********", "Latitude");
                        } else if (snap.getKey().equals("longitude")) {
                            mRest.setLatitude(snap.getValue(String.class));
                            Log.i("********", "longitude");
                            restHolder.add(mRest);
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("**********","Cancelled" );
            }
        });

        myAdapter.notifyDataSetChanged();


        //Floating action buttons
        fab = (FloatingActionButton) findViewById(R.id.fab);
        /* Placed in separate method
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (ActivityCompat.checkSelfPermission(Places.this, Manifest.permission.ACCESS_FINE_LOCATION) !=PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(Places.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    Log.i("Check Permissions", "Failed");
                    return;
                }
                else{

                    Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if(loc != null){
                        myLat = loc.getLatitude();
                        myLng = loc.getLongitude();
                        LatLng locn = new LatLng(myLat, myLng);
                        Toast.makeText(Places.this, "LocationGot", Toast.LENGTH_SHORT).show();
                        Log.i("Check Permissions", "Passed, Location grabbed");

                    }
                }

                //Calling Maps
                goMaps(v);
            }
        });
        */
        /*
        fabHistory = (FloatingActionButton) findViewById(R.id.fabHistory);
        fabHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Calling History
                Intent historyIntent = new Intent(Places.this, History.class);
                startActivity(historyIntent);
            }
        });
        */
    }

    public void openMaps(View view){
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(Places.this, Manifest.permission.ACCESS_FINE_LOCATION) !=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(Places.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            Log.i("Check Permissions", "Failed");
            return;
        }
        else{

            Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(loc != null){
                myLat = loc.getLatitude();
                myLng = loc.getLongitude();
                LatLng locn = new LatLng(myLat, myLng);
                //Toast.makeText(Places.this, "LocationGot", Toast.LENGTH_SHORT).show();
                Log.i("Check Permissions", "Passed, Location grabbed");

            }
        }

        //Calling Maps
        goMaps(view);
    }


    public void getLocation(String place, int position){
        DatabaseReference passLat = mBase.child(Integer.toString(position)+"/latitude");
        passLat.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot data) {
                mLat = data.getValue(String.class);
                Log.i("mLat", mLat);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //Get longitude
        DatabaseReference passLng = mBase.child(Integer.toString(position)+"/longitude");
        passLng.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot data) {
                mLng = data.getValue(String.class);
                Log.i("mLng", mLng);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    public void goMaps(View view){
        Intent intent = new Intent(Places.this, MapsActivity2.class);
        //Pass location data
        //String myLatS = Double.toString(myLat);
        //String myLngS = Double.toString(myLng);
        intent.putExtra("myLat", myLat);
        intent.putExtra("myLng", myLng);
        intent.putExtra("mLat", mLat);  //String
        intent.putExtra("mLng", mLng);  //String
        intent.putExtra("placeName", indexName); //String
        startActivity(intent);
    }




    }

