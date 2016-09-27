package com.example.scrollinglist;

import android.app.Application;

import com.example.scrollinglist.tve.TVEDelegate;
import com.vmn.android.bento.Bento;
import com.vmn.android.tveauthcomponent.component.TVEComponent;
import com.vmn.android.tveauthcomponent.model.TVEConfiguration;

/**
 * Created by belangek on 9/26/16.
 */
public class MainApplication extends Application {

    private static MainApplication instance;
    private TVEDelegate tve;
    private String country;

    public static MainApplication getInstance() {
        if (instance == null) throw new IllegalStateException(
                "Tried to reference Application object before it was created");
        return instance;

    }
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        tve = TVEDelegate.getInstance();
        country = "US";
        reconfigureTVEComponent("ComedyCentral");
        Bento.init(getApplicationContext()); // Note: this line is assuming that the
        // MainApplication class extends Application, as demonstrated above.
    }
    public void reconfigureTVEComponent (String brand) {
        TVEConfiguration.Builder builder = new
                TVEConfiguration.Builder(brand).setCountryCode(country);
        TVEConfiguration config = builder.build();
        TVEComponent.getInstance().initialize(this, tve, config);
    }
}