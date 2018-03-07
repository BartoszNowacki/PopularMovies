package com.example.rewan.recycler;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.rewan.R;
import com.example.rewan.ui.main.MainActivity;
import com.example.rewan.utils.ImagePathBuilder;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
     * Adapter for Movies from DataBase
     */
public class FavoriteMovieAdapter extends RecyclerView.Adapter<FavoriteMovieAdapter.MovieViewHolder> {

    private final Context mContext;
    private final MovieAdapterOnClickHandler mClickHandler;
    private Cursor mCursor;

    public interface MovieAdapterOnClickHandler {
        void onFavoriteClick(String movieId, View thumbnail, int position);
    }

    public FavoriteMovieAdapter(Context context, MovieAdapterOnClickHandler clickHandler) {
        this.mContext = context;
        this.mClickHandler = clickHandler;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.movie_entry, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        String posterPath = mCursor.getString(MainActivity.INDEX_MOVIE_POSTER);
        ImagePathBuilder pathBuilder = new ImagePathBuilder();
        Picasso.with(mContext)
                .load(pathBuilder.posterPathBuilder(posterPath))
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(holder.thumbnailIV);
    }

    @Override
    public int getItemCount() {
        if(mCursor == null) return 0;
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        this.mCursor = newCursor;
        notifyDataSetChanged();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.thumbnail_iv)
        ImageView thumbnailIV;

        public MovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            mCursor.moveToPosition(position);
            String movieId = mCursor.getString(MainActivity.INDEX_MOVIE_ID);
            mClickHandler.onFavoriteClick(movieId, thumbnailIV, position);
        }
    }

}
