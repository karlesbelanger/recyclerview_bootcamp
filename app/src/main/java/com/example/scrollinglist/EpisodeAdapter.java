package com.example.scrollinglist;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.scrollinglist.pojo.Episode;
import com.example.scrollinglist.pojo.EpisodeResponse;
import com.vmn.android.player.model.MGID;

import java.util.ArrayList;
import java.util.List;

/**
* Created by belangek on 9/22/16.
 */
public class EpisodeAdapter extends RecyclerView.Adapter<EpisodeAdapter.ViewHolder> {

    private List<Episode> mArrayList;
    private final String TAG = EpisodeAdapter.class.getSimpleName();
    private static Context mContext;


    private static String mStringMGID;


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView textViewTitle;
        private final TextView textViewDescription;
        private final TextView textViewDuration;
        private final TextView textViewRating;
        private final TextView textViewRatingType;
        private final TextView textViewGenre;

        private final ImageView imageView;

        public ViewHolder(final View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /****************
                     * To do
                     */
                    Intent intent = new Intent(mContext, EpisodePlayer.class);
                    intent.putExtra("MGIDString", mStringMGID);
                    mContext.startActivity(intent);
                }
            });
            textViewTitle       = (TextView) itemView.findViewById(R.id.r_title);
            textViewDescription = (TextView) itemView.findViewById(R.id.r_description);
            textViewDuration    = (TextView) itemView.findViewById(R.id.r_time);
            textViewRating      = (TextView) itemView.findViewById(R.id.r_rating);
            textViewRatingType     = (TextView) itemView.findViewById(R.id.r_rating_type);
            textViewGenre       = (TextView) itemView.findViewById(R.id.r_genre);

            imageView       = (ImageView) itemView.findViewById(R.id.r_image);

        }
    }

    public EpisodeAdapter(List<Episode> arrayList, Context context) {
        this.mContext = context;
        this.mArrayList = arrayList;
    }

    @Override
    public EpisodeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View termView = inflater.inflate(R.layout.recycler_item, parent, false);

        return new ViewHolder(termView);
    }

    @Override
    public void onBindViewHolder(EpisodeAdapter.ViewHolder holder, int position) {
        Episode episode = mArrayList.get(position);

        mStringMGID = episode.getMgid();
        TextView textViewTitle       = holder.textViewTitle;
        TextView textViewDescription = holder.textViewDescription;
        TextView textViewDuration    = holder.textViewDuration;
        TextView textViewRating      = holder.textViewRating;
        TextView textViewRatingType  = holder.textViewRatingType;
        TextView textViewGenre       = holder.textViewGenre;

        ImageView imageView          = holder.imageView;
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        textViewTitle.setText(episode.getTitle());
        textViewDescription.setText(episode.getDescription());


        textViewDuration.setText(convertIntToTime(episode.getDuration()));
        textViewRating.setText(episode.getRating());
        textViewRatingType.setText(episode.getRatingType());
        textViewGenre.setText(episode.getGenre());

        Glide.with(mContext)
                .load(episode.getImages().get(0).getUrl())
                .into(imageView);

    }

    private String convertIntToTime(int timeInt){
        int time = timeInt;
        int hours = time / 3600;
        time = time - (hours * 3600);
        int minutes = time / 60;
        time = time - (minutes * 60);
        int seconds = time ;
        return hours + ":" + (minutes<10? "0" :"") + minutes + ":" + (seconds<10? "0" :"") +  seconds;
    }
    @Override
    public int getItemCount() {

        return mArrayList.size();
    }



}