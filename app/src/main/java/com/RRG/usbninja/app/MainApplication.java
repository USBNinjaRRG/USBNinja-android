package com.RRG.usbninja.app;

import android.app.Application;
import android.content.Context;

import com.RRG.usbninja.LogInfo;

import java.util.ArrayList;

public class MainApplication extends Application {

    private static final String TAG = MainApplication.class.getName();

    public static MainApplication instance;

    public ArrayList<LogInfo> logList = new ArrayList<>();

    //get Application Context
    public static Context getAppContext() {
        return instance.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

    }
}
