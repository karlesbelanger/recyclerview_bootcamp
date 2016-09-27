package com.example.scrollinglist;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.vmn.android.bento.facade.Facade;
import com.vmn.android.freewheel.FreewheelPluginController;
import com.vmn.android.freewheel.impl.FWAdSlot;
import com.vmn.android.freewheel.impl.FreewheelPlugin;
import com.vmn.android.player.AndroidPlayerContext;
import com.vmn.android.player.VMNVideoPlayerImpl;
import com.vmn.android.player.api.PreparedContentItem;
import com.vmn.android.player.model.MGID;
import com.vmn.android.player.model.VMNContentSession;
import com.vmn.android.player.view.VideoSurfaceView;
import com.vmn.functional.Optional;
import com.vmn.log.PLog;
import com.vmn.util.Diagnostics;

import java.net.URI;

import tv.freewheel.ad.interfaces.IAdInstance;

public class EpisodePlayer extends AppCompatActivity {
    private static final String TAG = EpisodePlayer.class.getSimpleName();
    private String mStringMGID;
    private MGID episodeMGID;
    private String appName;
    private static final int MAX_BUFFER_SIZE_MEGS = 32;
    static final String MGID_STRING = "mgid_string";
    @NonNull
    private SingletonPlayer singleton;
    public static final String PLAYER_SESSION_KEY = "playerSession"; /*Optional: used to
    serialize and save the session to prevent reloading if the activity restarts such as
    due to a screen rotation*/

    private VideoSurfaceView videoPlayerView;
    private AndroidPlayerContext playerContext;
    private VMNVideoPlayerImpl videoPlayer;
    private VMNContentSession session = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.episode_player);
        videoPlayerView = (VideoSurfaceView) findViewById(R.id.video_player);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                mStringMGID = null;
            } else {
                mStringMGID = extras.getString("MGIDString");
                episodeMGID = MGID.make(mStringMGID);
                appName = getAppName(mStringMGID);
            }
        } else {
            mStringMGID = savedInstanceState.getString(MGID_STRING);
            //Log.d(TAG, "onCreate: " + mStringMGID);
            episodeMGID = MGID.make(mStringMGID);
            appName = getAppName(mStringMGID);
        }


        singleton = SingletonPlayer.getInstance();
        playerContext = singleton.getPlayerContext();

        Facade.getInstance().setActivity(this);

        videoPlayer = playerContext.buildPlayer()
                .autoPlay(true) // true iff each loaded video should start playing immediately regardless of prior player state
                .offScreenRender(false) // true iff video should keep playing when app goes to background/video surface is removed
                .responseLooper(Looper.getMainLooper()) // which thread should receive top-level player callbacks (does not affect plugins or secondary player callbacks, like ContentPlayer, CorePlayer, etc)
                .build();

        if (savedInstanceState != null) session = (VMNContentSession)
                savedInstanceState.getSerializable(PLAYER_SESSION_KEY); //Optional: used for saving state
        else session = playerContext.buildSession(episodeMGID, appName,
                false).authRequired(false).build(); //authRequired is false because we are only

        // Provide a reference to the base view that contains all the player visual components.  The player and its
        // various plugins will look for specific IDs within this view, and hook them up to specific functional elements.
        // The player wants to draw into an instance of VMNPlayerView with ID R.id.video_player, and so on.
        // Check res/layout/demo_player.xml for an example of how a typical player layout will work.
        videoPlayer.setView(Optional.<View>of(videoPlayerView));

        
        final Optional<FreewheelPlugin> freewheelPlugin =
                playerContext.findPlugin(FreewheelPlugin.class);
        if (freewheelPlugin.isPresent()) {
            // do i have to close emulator each time??


            //change to mtv?

            
            final Optional<FreewheelPluginController> freewheelPluginController =
                    freewheelPlugin.get().interfaceFor(videoPlayer);
            freewheelPlugin.get().getAdHolidayCounter().endAdHoliday(); //This can help force the mid-video "commercial break" ads to play
            freewheelPluginController.get().registerDelegate(new FreewheelPluginController.DelegateBase() {
                @Override
                public void instanceClickthroughTriggered(FWAdSlot slot, IAdInstance instance,
                                                          URI uri) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(uri.toString())));
                }
            });
            freewheelPluginController.get().registerDelegate(Diagnostics.loggingProxy(FreewheelPluginController.Delegate.class, PLog.VERBOSE)); //Optional: causes the ad player to log
            //// diagnostic information which can be helpful in seeing if it works or not
        }


        //viewing the free clips, as opposed to those requiring an account
//        videoPlayer.clear();
//        videoPlayer.enqueue(playerContext.prepareSession(session)); // Use the Freewheel plugin to receive ad clickthroughs.


//        final Optional<FreewheelPlugin> freewheelPlugin = playerContext.findPlugin(FreewheelPlugin.class);
//        if (freewheelPlugin.isPresent()) {
//            final Optional<FreewheelPluginController> freewheelPluginController = freewheelPlugin.get().interfaceFor(videoPlayer);
//            freewheelPluginController.get().registerDelegate(new FreewheelPluginController.DelegateBase() {
//                @Override
//                public void instanceClickthroughTriggered(FWAdSlot slot, IAdInstance instance, URI uri) {
//                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(uri.toString())));
//                }
//            });
//        }




        // Collect the session that is to be started from either the bundle or the Intent (the latter comes from
        // DemoSelector.java; the former from `onSaveInstanceState` of prior versions of this Activity).
        if (savedInstanceState != null)
            session = (VMNContentSession) savedInstanceState.getSerializable(PLAYER_SESSION_KEY);
        if (session == null)
            session = (VMNContentSession) getIntent().getSerializableExtra(PLAYER_SESSION_KEY);
        if (session == null) {
            //Log.e("EpisodePlayer", "Failed to load session from Bundle or Intent");
            finish();
        }

        // Check the shared location for an already-prepared item (see DemoSingleton.java).
        final Optional<PreparedContentItem> preparedItem = singleton.getPreparedItem(session);

        // If we had a prepared item, pass it into the player; if not, prepare a fresh one using the given session.
        videoPlayer.enqueue(preparedItem.isPresent()
                ? preparedItem.get()
                : playerContext.prepareSession(session));

        // Check this class for some specific enhancements and custom ways of using the player.
        //final DemoExtensions demoExtensions = new DemoExtensions(videoPlayer);

        // At this point, your video should be playing.  Any time spent by the user between looking at the selector
        // screen and loading a video will be used to prepare content, so video playback should be quite responsive.
        // Note however that we are contractually prevented from preparing Freewheel content in advance, so if prerolls
        // are in use there will be a minimum startup delay defined by the response time of the preroll slot.
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    /* Include the following function somewhere within your class. */
    private String getAppName(String mgidString) {
        String appName;
        if (mgidString.contains("mtv")) {
            appName = "MTV_App_Android";
        } else if (mgidString.contains("spike")) {
            appName = "Spike_TVE_App_Android";
        } else if (mgidString.contains("comedycentral")) {
            appName = "CC_TVE_App_Android_App";
        } else if (mgidString.contains("nickjr")) {
            appName = "CasNickJrApp_Staging";
        } else if (mgidString.contains("nick.com")) {
            appName = "Nick_App_Android_Phone";
        } else if (mgidString.contains("vh1")) {
            appName = "VH1_App_Android";
        } else if (mgidString.contains("bet")) {
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
        outState.putSerializable(PLAYER_SESSION_KEY, session);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        videoPlayer.setPlayWhenReady(false);
    }
}
