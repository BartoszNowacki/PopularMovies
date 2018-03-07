package com.example.rewan.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
 * Adapter for RecyclerView in Main Activity. This Adapter is used for feed from network api
 */
public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder>{

        private List<Movie> moviesList;
        private Context context;
         private final MovieAdapterOnClickHandler mClickHandler;

    public interface MovieAdapterOnClickHandler {
        void onClick(int movieId, View thumbnail, int position);
    }

        public MoviesAdapter(List<Movie> moviesList, Context context, MovieAdapterOnClickHandler clickHandler) {
            this.moviesList = moviesList;
            this.context = context;
            this.mClickHandler = clickHandler;
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
        }

        @Override
        public int getItemCount() {
            return ((moviesList != null) && (moviesList.size() != 0) ? moviesList.size() : 1);
        }

    /**
     * ViewHolder static class for RecyclerView
     */
    class MoviesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.thumbnail_iv)
        ImageView thumbnailIV;

        MoviesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Movie movieItem = moviesList.get(position);
            int movieId = Integer.parseInt(movieItem.getID());
            mClickHandler.onClick(movieId, thumbnailIV, position);
        }
    }
}
