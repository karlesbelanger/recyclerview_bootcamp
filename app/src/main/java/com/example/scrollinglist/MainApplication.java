package com.example.scrollinglist;

import android.app.Application;

/**
 * Created by belangek on 9/26/16.
 */
public class MainApplication extends Application {

    private static MainApplication instance;
    public static MainApplication getInstance() {
        if (instance == null) throw new IllegalStateException(
                "Tried to reference Application object before it was created");
        return instance;

    }
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}