package com.sunonline.util;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by duanjigui on 2016/8/3.
 */
public class ExampleApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
    }
}
