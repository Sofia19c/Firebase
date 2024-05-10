package com.example.nuevo_parchaosr.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nuevo_parchaosr.App;
import com.example.nuevo_parchaosr.services.LocationService;
import com.example.nuevo_parchaosr.services.RouterGoogleAPIService;
import com.example.nuevo_parchaosr.utils.AlertsHelper;
import com.example.nuevo_parchaosr.utils.PermissionHelper;

import javax.inject.Inject;

public abstract class BasicActivity extends AppCompatActivity {

    @Inject
    AlertsHelper alertsHelper;

    @Inject
    PermissionHelper permissionHelper;

    @Inject
    LocationService locationService;

    @Inject
    RouterGoogleAPIService routerGoogleAPIService;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        ((App) getApplicationContext()).getAppComponent().inject(this);
        super.onCreate(savedInstanceState);
    }
}
