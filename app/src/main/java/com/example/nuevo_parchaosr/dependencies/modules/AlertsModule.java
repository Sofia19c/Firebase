package com.example.nuevo_parchaosr.dependencies.modules;



import com.example.nuevo_parchaosr.utils.AlertsHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AlertsModule {
    @Singleton
    @Provides
    public AlertsHelper provideAlertHelper() {
        return new AlertsHelper();
    }
}
