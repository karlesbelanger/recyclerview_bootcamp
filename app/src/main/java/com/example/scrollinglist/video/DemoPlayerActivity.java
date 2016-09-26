package com.example.scrollinglist.video;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.example.scrollinglist.R;
import com.vmn.android.freewheel.FreewheelPluginController;
import com.vmn.android.freewheel.impl.FWAdSlot;
import com.vmn.android.freewheel.impl.FreewheelPlugin;
import com.vmn.android.player.AndroidPlayerContext;
import com.vmn.android.player.VMNVideoPlayerImpl;
import com.vmn.android.player.api.PreparedContentItem;
import com.vmn.android.player.model.VMNContentSession;
import com.vmn.android.player.view.VideoSurfaceView;
import com.vmn.functional.Optional;

import java.net.URI;

import tv.freewheel.ad.interfaces.IAdInstance;

/**
 * Created by belangek on 9/26/16.
 */
public class DemoPlayerActivity extends Activity {
    public static final String PLAYER_SESSION_KEY = "playerSession";

    //@Bind(R.id.video_player)
    VideoSurfaceView videoPlayerView;

    @NonNull private AndroidPlayerContext playerContext;
    @NonNull private VMNVideoPlayerImpl videoPlayer;
    @NonNull private DemoSingleton singleton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Assign a view that contains the necessary player visual components
        setContentView(R.layout.episode_player);

        //ButterKnife.bind(this);

        // Get an instance to the app-singleton player context instance
        singleton = DemoSingleton.getInstance();
        playerContext = singleton.getPlayerContext();

        // Create a new player for this activity (the player can also be shared across activities).  Builder pattern is
        // used here, allowing for custom player properties to be configured at the time of construction.  The values
        // shown here are the defaults, so you only need to specify these parameters if you want something different.
        videoPlayer = playerContext.buildPlayer()
                .autoPlay(true) // true iff each loaded video should start playing immediately regardless of prior player state
                .offScreenRender(false) // true iff video should keep playing when app goes to background/video surface is removed
                .responseLooper(Looper.getMainLooper()) // which thread should receive top-level player callbacks (does not affect plugins or secondary player callbacks, like ContentPlayer, CorePlayer, etc)
                .build();


        // Use the Freewheel plugin to receive ad clickthroughs.
        final Optional<FreewheelPlugin> freewheelPlugin = playerContext.findPlugin(FreewheelPlugin.class);
        if (freewheelPlugin.isPresent()) {
            final Optional<FreewheelPluginController> freewheelPluginController = freewheelPlugin.get().interfaceFor(videoPlayer);
            freewheelPluginController.get().registerDelegate(new FreewheelPluginController.DelegateBase() {
                @Override
                public void instanceClickthroughTriggered(FWAdSlot slot, IAdInstance instance, URI uri) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(uri.toString())));
                }
            });
        }


        // Provide a reference to the base view that contains all the player visual components.  The player and its
        // various plugins will look for specific IDs within this view, and hook them up to specific functional elements.
        // The player wants to draw into an instance of VMNPlayerView with ID R.id.video_player, and so on.
        // Check res/layout/demo_player.xml for an example of how a typical player layout will work.
        videoPlayer.setView(Optional.<View>of(videoPlayerView));

        // Collect the session that is to be started from either the bundle or the Intent (the latter comes from
        // DemoSelector.java; the former from `onSaveInstanceState` of prior versions of this Activity).
        VMNContentSession session = null;
        if (savedInstanceState != null) session = (VMNContentSession) savedInstanceState.getSerializable(PLAYER_SESSION_KEY);
        if (session == null) session = (VMNContentSession)getIntent().getSerializableExtra(PLAYER_SESSION_KEY);
        if (session == null) {
            Log.e("DemoPlayerActivity", "Failed to load session from Bundle or Intent");
            finish();
        }

        // Check the shared location for an already-prepared item (see DemoSingleton.java).
        final Optional<PreparedContentItem> preparedItem = singleton.getPreparedItem(session);

        // If we had a prepared item, pass it into the player; if not, prepare a fresh one using the given session.
        videoPlayer.enqueue(preparedItem.isPresent()
                ? preparedItem.get()
                : playerContext.prepareSession(session));

        // Check this class for some specific enhancements and custom ways of using the player.
        final DemoExtensions demoExtensions = new DemoExtensions(videoPlayer);

        // At this point, your video should be playing.  Any time spent by the user between looking at the selector
        // screen and loading a video will be used to prepare content, so video playback should be quite responsive.
        // Note however that we are contractually prevented from preparing Freewheel content in advance, so if prerolls
        // are in use there will be a minimum startup delay defined by the response time of the preroll slot.
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        final Optional<VMNContentSession> state = videoPlayer.getPlaybackState();
        if (state.isPresent()) outState.putSerializable(PLAYER_SESSION_KEY, state.get());
    }

    @Override
    protected void onPause() {
        super.onPause();
        videoPlayer.setPlayWhenReady(false);
    }

    // TODO: Add TVE and Bento integration to this demo application
}