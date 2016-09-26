package com.example.scrollinglist.video;

import android.util.Log;
import com.vmn.android.player.*;
import com.vmn.android.player.api.VMNPlayerDelegate;
import com.vmn.android.player.api.VMNVideoPlayer;
import com.vmn.android.player.model.VMNClip;
import com.vmn.android.player.model.VMNContentItem;
import com.vmn.util.ErrorHandler;
import com.vmn.util.PlayerException;

/**
 * Created by belangek on 9/26/16.
 */

/**
 * A detailed example showing how to create a custom plugin that responds to player behaviour.  When active this
 * plugin logs 'interruptible' player activity and changes to the player view structure.
 *
 * There are two main parts to a plugin: the {@link VMNPlayerPlugin} and the {@link PlayerPluginBinding}.  The former
 * is tied to the {@link AndroidPlayerContext} and represents the plugin as a part of the player framework; the latter
 * is created by {@link VMNPlayerPlugin} for each new player, and represents the binding between the player and the
 * plugin.  Most of the real work is done by the {@link PlayerPluginBinding}: you can think of the Plugin as a
 * 'plugin type', and the Binding as a 'plugin instance' to go with a player.  This lets the system separate the state
 * of more than one player instance at the same time.
 *
 * {@link PlayerPluginBinding} includes a number of methods that are invoked by the player on its own control thread
 * when certain things happen. This allows you to use conventional thread management strategies to implement long
 * running actions against the player, such as showing pre-roll ads, performing authorization checks and so on.
 * Throwing a {@link PlayerException} from these method with a level of {@link PlayerException.Level#CRITICAL}
 * will result in the exception being propagated through the player's {@link ErrorHandler}, and will end content
 * playback.  A {@link PlayerException.Level#NONFATAL} exception propagating will cancel the action itself but not end
 * content playback (unless the action was a load action); a {@link PlayerException.Level#FATAL} will trigger
 * destruction of the player instance.
 *
 * TODO: Error interruption handling as described above does not always function as expected.
 *
 * @author Mark McKenna &lt;mark.mckenna@redspace.com&gt;
 */
public class DemoPlugin extends VMNPlayerPluginBase<DemoPlugin.Interface> {
    private static final String TAG = DemoPlugin.class.getName();

    /**
     * This is the public interface of the plugin, accessible through {@link VMNPlayerPlugin#interfaceFor(VMNVideoPlayer)}.
     * The brand application will use this to interact with the plugin.
     */
    public interface Interface {
        /** Turns the plugin on and off.  While true, the plugin will report; while false it will not. */
        void setActive(boolean b);

        /** @return whether the plugin is active or not. */
        boolean isActive();
    }

    /** This class implements the plugin binding, which links to a specific player instance.  Most of what you intend
     * your plugin to do should be in this class; an instance of this is created with
     * {@link #playerCreated(VMNVideoPlayerImpl, VMNPlayerDelegate, PlayerPluginManager)} below. */
    private final class Binding extends PlayerPluginBindingBase<Interface> implements Interface {
        private final VMNVideoPlayerImpl player;

        private boolean active;

        Binding(VMNVideoPlayerImpl player) {
            this.player = player;
        }

        @Override public void setActive(boolean b) { active = b; }
        @Override public boolean isActive() { return active; }


        /** Brands will obtain a public controller for the plugin with {@link #interfaceFor(VMNVideoPlayer)}.  That
         * will indirectly invoke this method to provide that interface. */
        @Override
        public Interface getInterface() { return this; }


        /**
         * Optional: this gets invoked before a clip starts playing in the player.  This is done in-line with the
         * player's control thread, so blocking this method will also block the player from progressing.
         */
        @Override
        public void beforeClip(VMNContentItem item, VMNClip clip) {
            if (active) Log.d(TAG, String.format("Clip %s started.", clip.getTitle()));
        }

        /**
         * This method gets automatically called by the player when it is being destroyed.  This lets you clean up
         * after yourself without adding to the brand app's cleanup burden.
         */
        @Override
        public void close() {
            if (active) Log.d(TAG, String.format("Plugin linked to player %s closed.", player));
        }
    }

    /**
     * All player-released plugins have a method like this; it would be good form to maintain that in brand-custom
     * plugins.  This method creates a plugin instance and connects it to the player context; after this, all players
     * created using that context will have the appropriate set of plugins attached.
     *
     * @param playerContext The player context this plugin is bound to.
     *
     * @return A newly created and bound instance of the plugin.
     */
    public static DemoPlugin bindPlugin(AndroidPlayerContext playerContext) {
        final DemoPlugin plugin = new DemoPlugin(playerContext);
        playerContext.registerPlugin(plugin);
        return plugin;
    }


    private final AndroidPlayerContext playerContext;


    /** Private constructor to create a plugin instance. Use {@link #bindPlugin(AndroidPlayerContext)} above
     * to create and bind plugin instances. */
    private DemoPlugin(AndroidPlayerContext playerContext) {
        super(playerContext.getAppContext());
        this.playerContext = playerContext;
    }


    /** This method gets called each time a player is created that is eligible for this plugin.  It creates the binding
     * object for that player, which will do the actual work of the plugin. */
    @Override
    public PlayerPluginBinding<Interface> playerCreated(VMNVideoPlayerImpl player, VMNPlayerDelegate delegateRepeater, PlayerPluginManager playerPluginManager) {
        return new Binding(player);
    }

    /** This method is invoked by the {@link AndroidPlayerContext} when it is closed itself.  It allows the context
     * to clean up after all of its bound plugins; this means that the context owns the plugin once it is bound, and
     * the brand is no longer able to unlink or destroy that plugin. */
    @Override public void close() { }
}

