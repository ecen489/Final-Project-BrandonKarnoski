package com.example.hungry;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class History extends AppCompatActivity {

    ListView listHistory;
    ArrayAdapter myAdapter;
    String indexName = "";
    DatabaseReference mDatabase;
    FirebaseDatabase mFirebase;
    ArrayList<String> myHistory;
    ArrayList<String> myVisits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        //Firebase
        mFirebase = FirebaseDatabase.getInstance();
        mDatabase = mFirebase.getReference("history");

        //Listview
        listHistory = findViewById(R.id.listHistory);
        myHistory = new ArrayList<>();
        myAdapter = new ArrayAdapter<String>(History.this, android.R.layout.simple_list_item_1, myHistory);
        listHistory.setAdapter(myAdapter);
        //Pulls the history from firebase
        mDatabase.addValueEventListener(new ValueEventListener() {
            String ID;
            String visits;
            @Override
            public void onDataChange(@NonNull DataSnapshot data) {
                Log.i("**********","onDataChange" );

                for (DataSnapshot ds : data.getChildren()) {
                    Log.i("**********","first for loop" );
                    for(DataSnapshot snap : ds.getChildren()) {
                        //Log.i("***********", snap.getKey());
                        if (snap.getKey().equals("id")) {
                            ID = snap.getValue(String.class);
                            myHistory.add(ID);
                            Log.i("**********", ID);
                        } else if (snap.getKey().equals("visits")) {
                            visits = snap.getValue(String.class);
                            Log.i("***********", "visits");
                            myVisits.add(visits);
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("**********","Cancelled" );
            }
        });
        for(int i = 0; i < myHistory.size() ;i = i+1 ){
            String temp = myHistory.get(i) + ": " + myVisits.get(i);
            myHistory.set(i, temp);
        }
        myAdapter.notifyDataSetChanged();

        //Button
    }
}
