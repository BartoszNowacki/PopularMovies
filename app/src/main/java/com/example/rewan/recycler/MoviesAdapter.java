package com.example.rewan.recycler;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.example.rewan.model.Movie;
import com.example.rewan.R;
import com.example.rewan.utils.ImagePathBuilder;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Adapter for RecyclerView in Main Activity
 */
public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder>{

        private List<Movie> moviesList;
        private Context context;

        public MoviesAdapter(List<Movie> moviesList, Context context) {
            this.moviesList = moviesList;
            this.context = context;
        }

        @Override
        public MoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.movie_entry, parent, false);
            return new MoviesViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MoviesViewHolder holder, int position) {
            Movie movieEntry = moviesList.get(position);
            ImagePathBuilder pathBuilder = new ImagePathBuilder();
            Picasso.with(context)
                    .load(pathBuilder.posterPathBuilder(movieEntry.getMoviePoster()))
                    .error(R.drawable.placeholder)
                    .placeholder(R.drawable.placeholder)
                    .into(holder.thumbnailIV);
//            holder.thumbnailIV.setTransitionName(Movie.MovieTags.TRANSITION + position);
        }

        @Override
        public int getItemCount() {
            return ((moviesList != null) && (moviesList.size() != 0) ? moviesList.size() : 1);
        }

    /**
     * ViewHolder static class for RecyclerView
     */
    static class MoviesViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.thumbnail_iv)
        ImageView thumbnailIV;

        MoviesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
