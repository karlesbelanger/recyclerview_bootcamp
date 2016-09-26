package com.example.scrollinglist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.example.scrollinglist.pojo.Episode;
import com.example.scrollinglist.pojo.EpisodeResponse;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements EpisodesListener{

    private static final String TAG = MainActivity.class.getSimpleName();
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;

    private List<Episode> mArrayList;
    private EpisodeAdapter mBasicAdapter;

    private static final String API_KEY = "rsvA6TrDBB84UAI92oV6u4IYCEpREzk8ayuB8oIr";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.a_main_toolbar);
        setSupportActionBar(mToolbar);

        //mArrayList = new ArrayList<>();
        new RetrofitVid(this)
                .getEpisodes(API_KEY);




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

//        mArrayList = data;
//        mBasicAdapter = new EpisodeAdapter(mArrayList, this);
//        Log.d(TAG, "onsuccess test: " + mArrayList);
//        mRecyclerView = (RecyclerView) findViewById(R.id.a_main_recycler);
//
//        mRecyclerView.setAdapter(mBasicAdapter);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

    }

}