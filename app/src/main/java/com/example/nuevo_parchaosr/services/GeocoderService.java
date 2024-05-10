package com.example.nuevo_parchaosr.services;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.example.nuevo_parchaosr.utils.DistanceUtils;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

import lombok.Getter;

@Getter
public class GeocoderService {
    private static final String TAG = GeocoderService.class.getName();

    private static final int MAX_RESULTS = 20;
    private static final double DISTANCE_RADIUS_KM = 40.0d;
    private final Context context;
    private final Geocoder geocoder;

    public GeocoderService(Context context) {
        this.context = context;
        this.geocoder = new Geocoder(context);
    }

    public List<Address> findPlacesByName(String name) throws IOException {
        return geocoder.getFromLocationName(name, MAX_RESULTS);
    }

    public List<Address> finPlacesByNameInRadius(String name, LatLng centerPosition) throws IOException {
        LatLng upperLeftPosition = DistanceUtils.moveLatLngInKilometer(-DISTANCE_RADIUS_KM, -DISTANCE_RADIUS_KM, centerPosition);
        LatLng bottomRightPosition = DistanceUtils.moveLatLngInKilometer(DISTANCE_RADIUS_KM, DISTANCE_RADIUS_KM, centerPosition);
        return geocoder.getFromLocationName(name, MAX_RESULTS, upperLeftPosition.latitude, upperLeftPosition.longitude, bottomRightPosition.latitude, bottomRightPosition.longitude);
    }

    public List<Address> findByPosition(LatLng pos) throws IOException{
        return geocoder.getFromLocation(pos.latitude, pos.longitude, 1);
    }
}
