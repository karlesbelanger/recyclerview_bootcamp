package com.example.scrollinglist.video;


import com.example.scrollinglist.pojo.EpisodeResponse;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by belangek on 9/22/16.
 */

public class RetrofitVid implements Callback<EpisodeResponse> {

    private static final String BASE_URL = "https://api.viacom.com";
    private EpisodesListener listener;
    private EpisodesServices service;
    private String TAG = RetrofitVid.class.getSimpleName();

    public RetrofitVid(EpisodesListener listener) {
        this.listener = listener;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .build();

        service = retrofit.create(EpisodesServices.class);
        this.listener = listener;
    }

    public void getEpisodes(String key) {
        Call<EpisodeResponse> call = null;
        call = service.getEpisodes(key);
//        Log.d(TAG, "getEpisodes: " + call.toString());

        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<EpisodeResponse> call, Response<EpisodeResponse> response) {
        //Log.d(TAG, "onResponse: " + response.body());
        int statusCode = response.code();

        EpisodeResponse responseObj = response.body();
        // responseObj = response.body();//call.execute().body();
        listener.onSuccess(responseObj);
    }

    @Override
    public void onFailure(Call<EpisodeResponse> call, Throwable t) {
//        Log.d(TAG, "getFailure: " + call.toString());
        listener.onFailure(t.getLocalizedMessage());
    }

}
