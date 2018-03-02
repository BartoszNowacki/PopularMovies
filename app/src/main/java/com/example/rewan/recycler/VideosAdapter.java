package com.example.rewan.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rewan.R;
import com.example.rewan.model.Movie;
import com.example.rewan.model.Video;
import com.example.rewan.utils.ImagePathBuilder;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Adapter for RecyclerView in Main Activity
 */
public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.VideosViewHolder>{

        private List<Video> videosList;
        private Context context;

        public VideosAdapter(List<Video> videosList, Context context) {
            this.videosList = videosList;
            this.context = context;
        }

        @Override
        public VideosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.video_entry, parent, false);
            return new VideosViewHolder(view);
        }

        @Override
        public void onBindViewHolder(VideosViewHolder holder, int position) {
            Video videoEntry = videosList.get(position);
            holder.titleTV.setText(adjustStringLength(videoEntry.getVideoTitle()));
            ImagePathBuilder pathBuilder = new ImagePathBuilder();
            Picasso.with(context)
                    .load(pathBuilder.youTubeImagePathBuilder(videoEntry.getVideoEndpoint()))
                    .error(R.drawable.placeholder)
                    .placeholder(R.drawable.placeholder)
                    .into(holder.thumbnailIV);
        }

        @Override
        public int getItemCount() {
            return ((videosList != null) && (videosList.size() != 0) ? videosList.size() : 1);
        }

        public String adjustStringLength(String title){
            if (title.length()>20){
                return title.substring(0,20) + "...";
            }
            return title;
        }

    /**
     * ViewHolder static class for RecyclerView
     */
    static class VideosViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.thumbnail_iv)
        ImageView thumbnailIV;
        @BindView(R.id.title_tv)
        TextView titleTV;

        VideosViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
