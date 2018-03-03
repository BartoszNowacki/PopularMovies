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
import com.example.rewan.model.Review;
import com.example.rewan.utils.ImagePathBuilder;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Adapter for Reviews RecyclerView in DetailActivity
 */
public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsViewHolder>{

        private List<Review> reviewsList;

        public ReviewsAdapter(List<Review> reviewsList) {
            this.reviewsList = reviewsList;
        }

        @Override
        public ReviewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.review_entry, parent, false);
            return new ReviewsViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ReviewsViewHolder holder, int position) {
            Review reviewEntry = reviewsList.get(position);
            holder.authorTV.setText(reviewEntry.getReviewAuthor());
            holder.contentTV.setText(reviewEntry.getReviewContent());
        }

        @Override
        public int getItemCount() {
            return ((reviewsList != null) && (reviewsList.size() != 0) ? reviewsList.size() : 1);
        }

    /**
     * ViewHolder static class for RecyclerView
     */
    static class ReviewsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.author_tv)
        TextView authorTV;
        @BindView(R.id.content_tv)
        TextView contentTV;

        ReviewsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
