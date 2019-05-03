package com.example.hungry;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class PlaceListAdapter extends RecyclerView.Adapter<PlaceListAdapter.ViewHolder>{

    private ArrayList<String> mPlaces = new ArrayList<>();
    private Context mContext;
    private static final String TAG = "PlaceListAdapter";

    public PlaceListAdapter(Context mContext, ArrayList<String> mPlaces) {
        this.mPlaces = mPlaces;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_placelist, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        String mCurrent = mPlaces.get(position);

        holder.parentLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Log.d(TAG, "onClick: clicked");
                Toast.makeText(mContext, mPlaces.get(position),Toast.LENGTH_SHORT);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPlaces.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView placeName, placeAddr, placeLong, placeLat;
        RelativeLayout parentLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            placeName = itemView.findViewById(R.id.placeName);
            placeAddr = itemView.findViewById(R.id.placeAddr);
            placeLat = itemView.findViewById(R.id.placeLat);
            placeLong = itemView.findViewById(R.id.placeLong);
        }
    }
}
