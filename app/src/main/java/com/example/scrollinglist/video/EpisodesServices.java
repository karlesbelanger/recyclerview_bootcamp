package com.example.scrollinglist.video;

import com.example.scrollinglist.pojo.EpisodeResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

/**
 * Created by belangek on 9/22/16.
 */

public interface EpisodesServices {
    @GET("contents/v2/brands/cc/episodes?offset=0&limit=20")
    Call<EpisodeResponse> getEpisodes(@Header("x-api-key") String apiKey);
}

