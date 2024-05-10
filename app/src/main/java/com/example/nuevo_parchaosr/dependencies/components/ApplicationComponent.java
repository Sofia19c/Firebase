package com.example.nuevo_parchaosr.dependencies.components;

import com.example.nuevo_parchaosr.activities.BasicActivity;
import com.example.nuevo_parchaosr.dependencies.modules.AlertsModule;
import com.example.nuevo_parchaosr.dependencies.modules.GeocoderModule;
import com.example.nuevo_parchaosr.dependencies.modules.LocationModule;
import com.example.nuevo_parchaosr.dependencies.modules.PermissionModule;
import com.example.nuevo_parchaosr.dependencies.modules.RouterGoogleAPIModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AlertsModule.class, PermissionModule.class, GeocoderModule.class, LocationModule.class, RouterGoogleAPIModule.class})
public interface ApplicationComponent {
    void inject(BasicActivity activity);

}
