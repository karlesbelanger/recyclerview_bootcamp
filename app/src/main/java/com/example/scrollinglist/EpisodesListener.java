package com.example.scrollinglist;

import com.example.scrollinglist.pojo.EpisodeResponse;


/**
 * Created by belangek on 9/22/16.
 */

public interface EpisodesListener {
        void onSuccess(EpisodeResponse data);

        void onFailure(String errorMsg);
}
