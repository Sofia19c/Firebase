package com.example.nuevo_parchaosr.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import lombok.Getter;

@Getter
public class PermissionHelper {
    private static final String TAG = PermissionHelper.class.getName();

    static public final int PERMISSIONS_REQUEST_CAMERA = 1001;
    static public final int PERMISSIONS_REQUEST_CAMERA_QR = 4949;
    static public final int PERMISSIONS_REQUEST_GALLERY = 1002;
    static public final int PERMISSIONS_REQUEST_READ_CONTACTS = 2002;
    static public final int PERMISSIONS_LOCATION = 3003;
    static public final int PERMISSIONS_REQUEST_GALLERY_VIDEO = 1004;
    static public final int PERMISSIONS_REQUEST_VIDEO = 1003;

    public boolean mCameraPermissionGranted;
    public boolean mGalleryPermissionGranted;

    public boolean mLocationPermissionGranted;

    public void getCameraPermission(Activity activity) {
        if(checkPermission(activity, Manifest.permission.CAMERA)){
            mCameraPermissionGranted = true;
        } else ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, PERMISSIONS_REQUEST_CAMERA);
    }
    public void getCameraPermissionForQR(Activity activity) {
        if(checkPermission(activity, Manifest.permission.CAMERA)){
            mCameraPermissionGranted = true;
        } else ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, PERMISSIONS_REQUEST_CAMERA_QR);
    }
    public void getGalleryPermission(Activity activity) {
        if(checkPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)){
            mGalleryPermissionGranted = true;
        } else ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_GALLERY);
    }

    public void getLocationPermission(Activity activity){
        if(checkPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)){
            mLocationPermissionGranted = true;
        } else ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_LOCATION);
    }

    private boolean checkPermission(Activity activity, String manifestPermissions) {
        /*
         * Request the permission. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        Log.d(TAG, "checkPermission: attempting to get permission for ("+manifestPermissions+").");
        if (ContextCompat.checkSelfPermission(activity.getApplicationContext(), manifestPermissions) == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "checkPermission: permission "+manifestPermissions+" is already granted.");
            return true;
        } else {
            Log.d(TAG, "checkPermission: permission ("+manifestPermissions+") not granted, need to request it.");
            return false;
        }
    }

}