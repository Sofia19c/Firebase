package com.example.nuevo_parchaosr;

import android.app.Application;

import com.example.nuevo_parchaosr.dependencies.components.ApplicationComponent;
import com.example.nuevo_parchaosr.dependencies.components.DaggerApplicationComponent;
import com.example.nuevo_parchaosr.dependencies.modules.GeocoderModule;
import com.example.nuevo_parchaosr.dependencies.modules.LocationModule;

import lombok.Getter;

@Getter
public class App extends Application {
    ApplicationComponent appComponent = DaggerApplicationComponent.builder()
            .locationModule(new LocationModule(this))
            .geocoderModule(new GeocoderModule(this))
            .build();
}
