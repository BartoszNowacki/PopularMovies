package com.example.rewan.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


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
public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.CountriesViewHolder>{

        private List<Movie> countriesList;
        private Context context;

        public MoviesAdapter(List<Movie> countriesList, Context context) {
            this.countriesList = countriesList;
            this.context = context;
        }

        @Override
        public CountriesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.movie_entry, parent, false);
            return new CountriesViewHolder(view);
        }

        @Override
        public void onBindViewHolder(CountriesViewHolder holder, int position) {
            Movie movieEntry = countriesList.get(position);
            ImagePathBuilder pathBuilder = new ImagePathBuilder();
            Picasso.with(context)
                    .load(pathBuilder.pathBuilder(movieEntry.getMoviePoster()))
                    .error(R.drawable.placeholder)
                    .placeholder(R.drawable.placeholder)
                    .into(holder.thumbnailIV);
        }

        @Override
        public int getItemCount() {
            return ((countriesList != null) && (countriesList.size() != 0) ? countriesList.size() : 1);
        }

    /**
     * ViewHolder static class for RecyclerView
     */
    static class CountriesViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.thumbnail_iv)
        ImageView thumbnailIV;

        CountriesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
