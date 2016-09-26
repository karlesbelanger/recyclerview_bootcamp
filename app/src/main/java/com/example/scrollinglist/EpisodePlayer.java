package com.example.scrollinglist;

import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.vmn.android.player.AndroidPlayerContext;
import com.vmn.android.player.VMNVideoPlayerImpl;
import com.vmn.android.player.model.MGID;
import com.vmn.android.player.model.VMNContentSession;
import com.vmn.android.player.view.VideoSurfaceView;

public class EpisodePlayer extends AppCompatActivity {
    private static final String TAG = EpisodePlayer.class.getSimpleName();
    private String mStringMGID;
    private MGID episodeMGID;
    private String appName;
    private static final int MAX_BUFFER_SIZE_MEGS = 32;
    static final String MGID_STRING = "mgid_string";
    public static final String PLAYER_SESSION_KEY = "playerSession"; /*Optional: used to
    serialize and save the session to prevent reloading if the activity restarts such as
    due to a screen rotation*/

    private VideoSurfaceView videoPlayerView;
    private AndroidPlayerContext playerContext;
    private VMNVideoPlayerImpl videoPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.episode_player);


        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                mStringMGID= null;
            } else {
                mStringMGID = extras.getString("MGIDString");
                episodeMGID = MGID.make(mStringMGID);
                appName = getAppName(mStringMGID);
            }
        } else {
            mStringMGID=  savedInstanceState.getString(MGID_STRING);
            Log.d(TAG, "onCreate: " + mStringMGID);
            episodeMGID = MGID.make(mStringMGID);
            appName = getAppName(mStringMGID);
        }



        VMNContentSession session = null;
        playerContext = new AndroidPlayerContext(
                this.getApplication(),
                "demoAdvertisingId");
        //this.getClass().getInstance().getTveService().getAuthBridge(),
        //MAX_BUFFER_SIZE_MEGS);
        //session = playerContext.buildSession(episodeMGID, appName,
        //       false).authRequired(false).build(); //authRequired is false because we are only
        //viewing the free clips, as opposed to those requiring an account

        videoPlayer = playerContext.buildPlayer()
                .autoPlay(true) // true iff each loaded video should start playing immediately regardless of prior player state
                .offScreenRender(false) // true iff video should keep playing when app goes to background/video surface is removed
                .responseLooper(Looper.getMainLooper()) // which thread should receive top-level player callbacks (does not affect plugins or secondary player callbacks, like ContentPlayer, CorePlayer, etc)
                .build();

        if (savedInstanceState != null) session = (VMNContentSession)
                savedInstanceState.getSerializable(PLAYER_SESSION_KEY); //Optional: used for saving state
        else session = playerContext.buildSession(episodeMGID, appName,
                false).authRequired(false).build(); //authRequired is false because we are only
        //viewing the free clips, as opposed to those requiring an account
        videoPlayer.clear();
        videoPlayer.enqueue(playerContext.prepareSession(session));
    }

    @Override
    protected void onStart() {
        super.onStart();
        videoPlayer.setPlayWhenReady(true);
    }

    /* Include the following function somewhere within your class. */
    private String getAppName(String mgidString) {
        String appName;
        if(mgidString.contains("mtv")){
            appName = "MTV_App_Android";
        } else if(mgidString.contains("spike")){
            appName = "Spike_TVE_App_Android";
        } else if(mgidString.contains("comedycentral")){
            appName = "CC_TVE_App_Android_App";
        } else if(mgidString.contains("nickjr")){
            appName = "CasNickJrApp_Staging";
        } else if(mgidString.contains("nick.com")){
            appName = "Nick_App_Android_Phone";
        } else if(mgidString.contains("vh1")){
            appName = "VH1_App_Android";
        } else if(mgidString.contains("bet")){
            appName = "BET_App_Android";
        } else {
//default
            appName = "MTV_App_Android";
        }
        return appName;
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //Log.d(TAG, "onRestoreInstanceState: " + mStringMGID);
        outState.putString(MGID_STRING, mStringMGID);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        videoPlayer.setPlayWhenReady(false);
    }
}
