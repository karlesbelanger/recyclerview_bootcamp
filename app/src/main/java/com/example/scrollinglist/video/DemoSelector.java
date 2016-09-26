//package com.example.scrollinglist.video;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.ListView;
//
//import com.example.scrollinglist.R;
//import com.vmn.android.player.AndroidPlayerContext;
//import com.vmn.android.player.model.VMNContentSession;
//import com.vmn.log.PLog;
//import com.vmn.util.Utils;
//
//import java.net.URI;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by belangek on 9/26/16.
// */
//
//public class DemoSelector extends Activity {
//    private static final VideoSourceCollector.PlatformSource platform =
//            new VideoSourceCollector.PlatformSource(URI.create("https://www.dropbox.com/s/17v73k20dmkzvj9/entertainment.json?dl=1"), "");
//
//    private final String TAG = Utils.generateTagFor(this);
//
//    //@Bind(R.id.demo_selector_list)
//    ListView videoList;
//
//
//    private AndroidPlayerContext playerContext;
//    private VideoSourceCollector sourceCollector;
//    private DemoSingleton singleton;
//    private ArrayAdapter<VMNContentSession> adapter;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.demo_selector);
//
//        //ButterKnife.bind(this);
//
//        // Define a location that can hold prepared content, shared across activities
//        singleton = DemoSingleton.getInstance();
//
//        playerContext = singleton.getPlayerContext();
//
//        sourceCollector = new VideoSourceCollector(playerContext.getHttpService());
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        // Fetch or otherwise build your list of videos that the user is choosing between
//        sourceCollector.getVideoSourcesFor(platform).notify(item -> {
//            final List<VideoSourceCollector.VideoSource> sources = item.get();
//
//            final List<VMNContentSession> sessions = new ArrayList<>(sources.size());
//
//            // Build a session for each one, and prepare those sessions that you want preload functionality for
//            // Store the prepared sessions in your shared location.
//            for (VideoSourceCollector.VideoSource source : sources) {
//                try {
//                    final VMNContentSession session = playerContext.buildSession(source.mgid, source.appName, false)
//                            .authRequired(false)
//                            .build();
//                    singleton.addPreparedItem(playerContext.prepareSession(session));
//                    sessions.add(session);
//                } catch (RuntimeException e) {
//                    PLog.e(TAG, "Error building session for MGID " + source.mgid, e);
//                }
//            }
//
//            adapter = new ArrayAdapter<VMNContentSession>(getBaseContext(), R.layout.spinner_item, sessions) {
//                public View getView(int position, View convertView, ViewGroup parent) {
//                    final VMNContentSession session = sessions.get(position);
//                    return Widgets.textView(DemoSelector.this)
//                            .textString(session.getContentMgid().toString())
//                            .layoutParams(Widgets.listViewLayoutParams().build())
//                            .onClick(v -> {
//                                final Intent i = new Intent(DemoSelector.this, DemoPlayerActivity.class);
//                                i.putExtra(DemoPlayerActivity.PLAYER_SESSION_KEY, session);
//                                startActivity(i);
//                            })
//                            .build();
//                }
//            };
//
//            runOnUiThread(() -> videoList.setAdapter(adapter));
//        });
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        singleton.clearPreparedItems();
//    }
//}
