package com.example.scrollinglist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.scrollinglist.pojo.Episode;
import com.example.scrollinglist.pojo.EpisodeResponse;
import com.example.scrollinglist.tve.LoginActivity;
import com.example.scrollinglist.tve.TVEDelegate;
import com.example.scrollinglist.video.EpisodeAdapter;
import com.example.scrollinglist.video.EpisodesListener;
import com.example.scrollinglist.video.RetrofitVid;
import com.vmn.android.tveauthcomponent.component.TVEComponent;
import com.vmn.android.tveauthcomponent.error.TVEException;
import com.vmn.android.tveauthcomponent.error.TVEMessage;
import com.vmn.android.tveauthcomponent.model.TVESubscriber;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements EpisodesListener, TVEDelegate.TveEventListener, SearchView.OnQueryTextListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;

    private List<Episode> favourites;
    private boolean isFavorite = false;
    private List<Episode> mArrayList;
    private EpisodeAdapter mBasicAdapter;

    private TVEDelegate tve;

    private static final String API_KEY = "rsvA6TrDBB84UAI92oV6u4IYCEpREzk8ayuB8oIr";
    private MenuItem loginBtn;
    private String loginTitle = "Login";

    private static final String[] COUNTRIES = new String[]{"Belgium",
            "France", "France_", "Italy", "Germany", "Spain"};
    private Menu menu;
    private String filterText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        tve = TVEDelegate.getInstance();
        tve.subscribeEventListener(this);
    }

//    private void toolbarSearchSetup(){
//        ActionBar actionBar = getActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setDisplayShowCustomEnabled(true);
//        // actionBar.setDisplayShowTitleEnabled(false);
//        // actionBar.setIcon(R.drawable.ic_action_search);
//
//        LayoutInflater inflator = (LayoutInflater) this
//                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View v = inflator.inflate(R.layout.actionbar, null);
//
//        actionBar.setCustomView(v);
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_dropdown_item_1line, COUNTRIES);
//        AutoCompleteTextView textView = (AutoCompleteTextView) v
//                .findViewById(R.id.editText1);
//        textView.setAdapter(adapter);

    //    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();

        new RetrofitVid(this)
                .getEpisodes(API_KEY);
        mToolbar = (Toolbar) findViewById(R.id.a_main_toolbar);
        setSupportActionBar(mToolbar);
        //toolbarSearchSetup();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MenuItem searchItem = menu.findItem(R.id.action_search);
        MenuItemCompat.collapseActionView(searchItem);
    }

    @Override
    public void onFailure(String errorMsg) {
        Toast.makeText(this, "Error: Please try again later.", Toast.LENGTH_LONG).show();
    }


    @Override
    public void onSuccess(EpisodeResponse data) {
        List<Episode> temp = data.getResponse().getEpisodes();
        if(isFavorite) {
            temp = filter(temp, filterText);
            if (favourites != null && favourites.size() <= 0) {
                Toast.makeText(this, "No Episode with the name of " + filterText + " found!", Toast.LENGTH_LONG).show();
            }
        }
        if (!isUserLoggedIn()) {
            mArrayList = new ArrayList<Episode>();
            for (Episode episode : temp) {
                if (episode.getPlatforms().size() > 0 && episode.getPlatforms().get(0).getAuthRequired().equals("false"))
                    mArrayList.add(episode);
            }
        } else {
            mArrayList = temp;
        }
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

    @Override//
    public void loginCompleted(TVESubscriber subscriber) {
        loginTitle = "Logout";
        Log.d(TAG, "loginCompleted: " + isUserLoggedIn());
    }

    @Override
    public void logoutCompleted() {
//TODO: tell your recycler adapter to refresh itself, now that it has lost permissions
        loginTitle = "Login";
        Log.d(TAG, "logoutCompleted: " + isUserLoggedIn());
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
        loginTitle = "Logout";
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

    private List<Episode> filter(List<Episode> episodes, String query) {
        query = query.toLowerCase();

        final List<Episode> filteredEpisodeList = new ArrayList<>();
        for (Episode episode : episodes) {
            final String text = episode.getTitle().toLowerCase();
            if (text.contains(query)) {
                filteredEpisodeList.add(episode);
            }
        }
        return filteredEpisodeList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        searchView.setOnSearchClickListener(searchClickListener);

        return true;
    }

    private View.OnClickListener searchClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (true) {
                favourites = mArrayList;//have api call get episode here
            }
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_login) {
            if (item.getTitle().equals("Login")) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                item.setTitle(loginTitle);
            } else {
                tve.unsubscribeEventListener(this);
                TVEComponent.getInstance().logout();
                item.setTitle(loginTitle);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        new RetrofitVid(this)
                .getEpisodes(API_KEY);
        mToolbar = (Toolbar) findViewById(R.id.a_main_toolbar);
        setSupportActionBar(mToolbar);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText.length() > 0) {
            isFavorite = true;
        } else
            isFavorite = false;
        return false;
    }

}