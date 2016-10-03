package com.example.scrollinglist.video;

import android.util.Log;

import com.example.scrollinglist.MainApplication;
import com.vmn.android.bento.Bento;
import com.vmn.android.freewheel.impl.FreewheelPlugin;
import com.vmn.android.player.AndroidPlayerContext;
import com.vmn.android.player.api.PreparedContentItem;
import com.vmn.android.player.controls.MediaControlsPlugin;
import com.vmn.android.player.model.VMNContentSession;
import com.vmn.android.player.plugin.bento.BentoPlugin;
import com.vmn.android.player.plugin.captions.CaptionsPlugin;
import com.vmn.functional.Optional;
import com.vmn.util.ErrorHandler;
import com.vmn.util.PlayerException;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by belangek on 9/26/16.
 */

public class SingletonPlayer {
    private static final SingletonPlayer instance = new SingletonPlayer();
    private static final String TAG = SingletonPlayer.class.getSimpleName();

    public static SingletonPlayer getInstance() { return instance; }


    // Define a location to store prepared items. The session makes a good key for this map.
    private final Map<VMNContentSession, PreparedContentItem> preparedContent = new HashMap<>();


    public void addPreparedItem(PreparedContentItem item) {
        preparedContent.put(item.getSession(), item);
    }

    // Define an operation to retrieve prepared content.  It's a good idea to throw away the prepared item after it
    // is returned, because it is usually only practical to use it once (the prepared item 'remembers' failures, so
    // future attempts to load a failed prepared item will instantly fail).
    public Optional<PreparedContentItem> getPreparedItem(VMNContentSession session) {
        try {
            return Optional.from(preparedContent, session);
        } finally {
            preparedContent.remove(session); // A prepared item is only eligible to be used once
        }
    }

    public void clearPreparedItems() { preparedContent.clear(); }

    private static final int MAX_BUFFER_SIZE_MEGS = 32;

    // Define a player context that provides the necessary bindings into application/Google services
    private final AndroidPlayerContext androidPlayerContext = new AndroidPlayerContext(
            MainApplication.getInstance(),
            "demoAdvertisingId");//,
//    MainApplication.getInstance().getTveService().getAuthBridge(),
//    MAX_BUFFER_SIZE_MEGS);

    public AndroidPlayerContext getPlayerContext() { return androidPlayerContext; }

    // Turn this flag ON to skip the preroll and turn it off see the preroll ad playback. This flag is added for preroll optimization
    // so if 'skipPreroll' is true and it's the first clip, ad context will still be fetched but beforeClip will not be blocked.
    // If above condition is false, beforeClip will be blocked until
    private final boolean skipPreroll = true;

    // For each optional player module you would like to use, invoke bindPlugin() on it, passing in the appropriate
    // configuration options to each.
    private SingletonPlayer() {
        // Any time a trapped exception happens through the player, notify Crashlytics.
        androidPlayerContext.getErrorHandler().registerDelegate(new ErrorHandler.Delegate() {
            @Override
            public void exceptionOccurred(PlayerException exception) {
                Log.e(TAG, "exceptionOccurred: ", exception );
            }
        });


        Bento.setPlayerContext(androidPlayerContext);

        // Link all plugins that each player instance will use
        CaptionsPlugin.bindPlugin(androidPlayerContext);
        FreewheelPlugin.bindPlugin(androidPlayerContext,
                FreewheelPlugin.HandleClicks.APPLICATION, skipPreroll);
        MediaControlsPlugin.bindPlugin(androidPlayerContext);
        BentoPlugin.bindPlugin(androidPlayerContext);

    }
}