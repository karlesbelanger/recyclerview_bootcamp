package com.example.scrollinglist.video;

import android.support.annotation.NonNull;
import com.vmn.android.player.VMNPlayerDelegateBase;
import com.vmn.android.player.VMNVideoPlayerImpl;
import com.vmn.android.player.model.PlayheadInterval;
import com.vmn.android.player.model.PlayheadPosition;
import com.vmn.android.player.model.VMNContentItem;
import com.vmn.android.player.util.CuepointSet;
import com.vmn.functional.Consumer;
import com.vmn.functional.Optional;
import com.vmn.functional.Predicate;

import java.util.concurrent.TimeUnit;

/**
 * Created by belangek on 9/26/16.
 */

public class DemoExtensions {
    @NonNull private final VMNVideoPlayerImpl videoPlayer;

    // The cuepoint set, to be constructed per content item
    private CuepointSet cuepointSet;

    // Construct the builder
    private final CuepointSet.Builder cuepointBuilder = new CuepointSet.Builder();

    public DemoExtensions(@NonNull final VMNVideoPlayerImpl videoPlayer) {
        this.videoPlayer = videoPlayer;

        // Register a delegate base to add a cuepoint when loading a content item
        videoPlayer.registerDelegate(delegate);
    }

    private final VMNPlayerDelegateBase delegate = new VMNPlayerDelegateBase() {
        @Override public void didLoadContentItem(final VMNContentItem contentItem) {
            // Create a playhead position representing the desired location of the cuepoint (example: First clip at 10 seconds)
            final PlayheadPosition cuepointPlayheadPosition = PlayheadPosition.indexedPosition(0, 10000, TimeUnit.MILLISECONDS);

            // Adding the cue points
            cuepointBuilder.addCuePoint(
                    new Predicate<PlayheadInterval>() { // The condition for this cuepoint callback (consumer) to be invoked.
                        @Override
                        public boolean test(PlayheadInterval interval) {
                            return interval.contains(
                                    contentItem,
                                    cuepointPlayheadPosition
                            );
                        }
                    },
                    new Consumer<PlayheadInterval>() { // The callback indicating the cuepoint has fired
                        @Override
                        public void accept(PlayheadInterval playheadInterval) {
                            // Handle as desired
                        }
                    }
            );

            // The cuepoints have been added. Constuct the cuepointset and set the video player
            cuepointSet = cuepointBuilder.build();
            cuepointSet.setVideoPlayer(Optional.of(videoPlayer));
        }

        @Override public void didUnloadContentItem(VMNContentItem contentItem) {
            cuepointSet.close();
        }
    };
}