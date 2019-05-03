package com.example.hungry;

import java.io.Serializable;

public class Restaurant implements Serializable {
    String name;
    String latitude;
    String longitude;

    public Restaurant(String placeName, String placeLat, String placeLong){
        name = placeName;
        latitude = placeLat;
        longitude = placeLong;

    }

    public String getName() {
        return name;
    }
    public String getLatitude(){
        return latitude;
    }
    public String getLongitude(){
        return longitude;
    }

    public void setName(String newName){
        name = newName;
    }
    public void setLatitude(String newLat){
        latitude = newLat;
    }
    public void setLongitude(String newLong){
        longitude = newLong;
    }

}
