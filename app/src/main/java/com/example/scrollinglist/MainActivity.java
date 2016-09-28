package com.example.scrollinglist;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.example.scrollinglist.pojo.Episode;
import com.example.scrollinglist.pojo.EpisodeResponse;
import com.example.scrollinglist.tve.TVEDelegate;
import com.example.scrollinglist.video.EpisodeAdapter;
import com.example.scrollinglist.video.EpisodesListener;
import com.example.scrollinglist.video.RetrofitVid;
import com.vmn.android.tveauthcomponent.component.TVEComponent;
import com.vmn.android.tveauthcomponent.error.TVEException;
import com.vmn.android.tveauthcomponent.error.TVEMessage;
import com.vmn.android.tveauthcomponent.model.TVESubscriber;

import java.util.List;


public class MainActivity extends AppCompatActivity implements EpisodesListener, TVEDelegate.TveEventListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;

    private List<Episode> mArrayList;
    private EpisodeAdapter mBasicAdapter;

    private TVEDelegate tve;

    private static final String API_KEY = "rsvA6TrDBB84UAI92oV6u4IYCEpREzk8ayuB8oIr";

/*    private FragmentManager.OnBackStackChangedListener backStackListener = new FragmentManager.OnBackStackChangedListener() {
        @Override
        public void onBackStackChanged() {
            setNavIcon();
        }
    };*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        tve = TVEDelegate.getInstance();

        mToolbar = (Toolbar) findViewById(R.id.a_main_toolbar);
        setSupportActionBar(mToolbar);
/*
        if (true) {
            mToolbar.setNavigationIcon(R.drawable.back);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }


        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        setUpNavigationDrawer();

        getFragmentManager().addOnBackStackChangedListener(backStackListener); // listen to the backstack of the fragment manager
*/

        new RetrofitVid(this)
                .getEpisodes(API_KEY);

    }

    @Override
    protected void onResume() {
        super.onResume();
        tve.subscribeEventListener(this);
        if (tve.isInitialized()) {
            TVEComponent.getInstance().checkStatus(false);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
//        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//        startActivity(intent);
//        if (isUserLoggedIn()) {
//
//            new RetrofitVid(this)
//                    .getEpisodes(API_KEY);
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        tve.unsubscribeEventListener(this);
        //not sure to put logout here
        TVEComponent.getInstance().logout();
    }

    @Override
    public void onFailure(String errorMsg) {

        Toast.makeText(this, "Error: Please try again later.", Toast.LENGTH_LONG).show();
        //Log.d(TAG, "onFailure: " + errorMsg);
    }


    @Override
    public void onSuccess(EpisodeResponse data) {
        mArrayList = data.getResponse().getEpisodes();
        mBasicAdapter = new EpisodeAdapter(mArrayList, this);
        Log.d(TAG, "onsuccess test: " + mArrayList);
        mRecyclerView = (RecyclerView) findViewById(R.id.a_main_recycler);

        mRecyclerView.setAdapter(mBasicAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    // TVE Callbacks
    @Override
    public void initializationCompleted(TVESubscriber subscriber) {
    }

    @Override
    public void checkStatusCompleted(TVESubscriber subscriber) {
    }

    @Override
    public void loginFormPrepared(Fragment tveFragment) {
    }

    @Override
    public void loginCompleted(TVESubscriber subscriber) {
    }

    @Override
    public void logoutCompleted() {
//TODO: tell your recycler adapter to refresh itself, now that it has lost permissions
    }

    @Override
    public void errorHappened(TVEException error) {
    }

    @Override
    public void onDisplayMessage(TVEMessage message) {
    }

    @Override
    public void onUserIdChanged(String userId) {
    }

    @Override
    public void learnMoreButtonClicked() {
    }

    @Override
    public void watchNowButtonClicked() {
    }

    @Override
    public void closeButtonClicked() {
    }

    @Override
    public void freePreviewHasJustExpired() {
    }

    public boolean isUserLoggedIn() {
        return tve.getCurrentTveSubscriber() != null &&
                tve.getCurrentTveSubscriber().isLoggedIn();

    }

  /*  protected void setNavIcon() {
        int backStackEntryCount = getFragmentManager().getBackStackEntryCount();
        drawerToggle.setDrawerIndicatorEnabled(backStackEntryCount == 0);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.isDrawerIndicatorEnabled() && drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case x:
                return true;
            default:
                return false;
        }
    }
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }*/
}