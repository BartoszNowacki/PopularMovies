package com.example.rewan.ui.detail;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.rewan.data.MovieContract;
import com.example.rewan.ui.BaseActivity;
import com.google.android.youtube.player.YouTubeStandalonePlayer;

import com.example.rewan.R;
import com.example.rewan.model.Review;
import com.example.rewan.model.Video;
import com.example.rewan.recycler.OnRecyclerClickListener;
import com.example.rewan.recycler.RecyclerItemClickListener;
import com.example.rewan.recycler.ReviewsAdapter;
import com.example.rewan.recycler.VideosAdapter;
import com.example.rewan.retrofit.DataService;
import com.example.rewan.retrofit.RetrofitHelper;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Retrofit;


/**
 * Class for details of Movie object
 */
public class DetailActivity
        extends BaseActivity
        implements DetailContract.View, OnRecyclerClickListener {

    @BindView(R.id.poster_iv)
    ImageView posterIV;
    @BindView(R.id.title_tv)
    TextView titleTV;
    @BindView(R.id.release_tv)
    TextView releaseTV;
    @BindView(R.id.vote_tv)
    TextView voteTV;
    @BindView(R.id.plot_tv)
    TextView plotTV;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.videos_recycler_view)
    RecyclerView videosRecyclerView;
    @BindView(R.id.content_layout)
    ConstraintLayout contentLayout;
    @BindView(R.id.reviews_recycler_view)
    RecyclerView reviewsRecyclerView;
    @BindView(R.id.rating)
    RatingBar ratingBar;
    @BindView(R.id.favorite_button)
    ToggleButton favoriteBTN;


    String TAG = "DetailMovie";
    DetailPresenter detailPresenter;

    String title;
    String posterEndpoint;
    String backdropEndpoint;
    String release;
    String vote;
    String plot;
    String movieID;
    String selection;
    List<Video> videosList;
    List<Review> reviewsList;
    private DataService dataService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Log.d(TAG, "onCreate: called");
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setupRetrofit();
        setupVideosRecyclerView();
        setupReviewsRecyclerView();

        String apiKey = getString(R.string.movie_api_key);
        detailPresenter = new DetailPresenter(dataService, apiKey);
        detailPresenter.attachView(this);

        if (savedInstanceState == null) {
            getFromExtras();
        } else {
            getFromInstanceState(savedInstanceState);
        }
        setupFavoriteButton();
        detailPresenter.setID(movieID);
        setView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(detailPresenter.getReceiver(), detailPresenter.getIntentFilter());
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(detailPresenter.getReceiver());
    }

    void getFromExtras() {
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            Log.d(TAG, "onCreate:  no extra");
        } else {
            title = extras.getString(detailPresenter.getTitle());
            release = extras.getString(detailPresenter.getRelease());
            vote = extras.getString(detailPresenter.getVote());
            plot = extras.getString(detailPresenter.getPlot());
            posterEndpoint = extras.getString(detailPresenter.getPoster());
            backdropEndpoint = extras.getString(detailPresenter.getBackdrop());
            movieID = extras.getString(detailPresenter.getID());
        }
    }

    void getFromInstanceState(Bundle savedInstanceState) {
        title = (String) savedInstanceState.getSerializable(detailPresenter.getTitle());
        release = (String) savedInstanceState.getSerializable(detailPresenter.getRelease());
        vote = (String) savedInstanceState.getSerializable(detailPresenter.getVote());
        plot = (String) savedInstanceState.getSerializable(detailPresenter.getPlot());
        posterEndpoint = (String) savedInstanceState.getSerializable(detailPresenter.getPoster());
        backdropEndpoint = (String) savedInstanceState.getSerializable(detailPresenter.getBackdrop());
        movieID = (String) savedInstanceState.getSerializable(detailPresenter.getID());
        posterIV.setTransitionName((String) savedInstanceState.getSerializable(detailPresenter.getID()));
    }
     /**
     * Setup for retrofit instance
     */
    public void setupRetrofit() {
        Retrofit retrofit = ((RetrofitHelper) getApplication()).getRetrofitInstance();
        dataService = retrofit.create(DataService.class);
    }
     /**
     * Sets view components
     */
    public void setView() {
        setTitle(title);
        titleTV.setText(title);
        releaseTV.setText(detailPresenter.getReleaseYear(release));
        voteTV.setText(vote);
        ratingBar.setRating(detailPresenter.convertToFloat(vote));
        plotTV.setText(plot);
        Picasso.with(this)
                .load(createImagePath())
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(posterIV);
    }
    /**
     * Method returns proper image path for Landscape or Portrait mode
     */
    public String createImagePath(){
        String imagePath;
        if (isLandscapeMode()){
                imagePath = detailPresenter.getPosterPath(backdropEndpoint);
        } else {
                imagePath = detailPresenter.getPosterPath(posterEndpoint);
        return imagePath;
        }
    }
     /**
     * Sets RecyclerView for videos
     */
    private void setupVideosRecyclerView() {
        videosRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(DetailActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        videosRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, videosRecyclerView, this));
        videosRecyclerView.setLayoutManager(layoutManager);
        videosRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }
     /**
     * Sets adapter for videos
     */
    public void setVideosAdapter(List<Video> videos) {
        videosList = videos;
        if (!videosList.isEmpty()) {
            VideosAdapter videosAdapter = new VideosAdapter(videos, this);
            videosRecyclerView.setAdapter(videosAdapter);
        }
    }
     /**
     * Sets adapter for reviews
     */
    public void setReviewsAdapter(List<Review> reviews) {
        reviewsList = reviews;
        if (!reviewsList.isEmpty()) {
            ReviewsAdapter reviewsAdapter = new ReviewsAdapter(reviews);
            reviewsRecyclerView.setAdapter(reviewsAdapter);
        }
    }
     /**
     * Sets RecyclerView for videos
     */
    private void setupReviewsRecyclerView() {
        reviewsRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(DetailActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        reviewsRecyclerView.setLayoutManager(layoutManager);
        reviewsRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }
     /**
     * Setup for favorite button
     */
    private void setupFavoriteButton() {
        if (IsFavorite()) {
            favoriteBTN.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), android.R.drawable.btn_star_big_on));
            favoriteBTN.setChecked(true);
        } else {
            favoriteBTN.setChecked(false);
            favoriteBTN.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), android.R.drawable.btn_star_big_off));
        }
        favoriteBTN.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                  favoriteButtonActive();
                } else {
                  favoriteButtonInactive();
                }
                String[] projection = {MovieContract.MovieEntry.COLUMN_TITLE};
                Log.d(TAG, "setupFavoriteButton: " + getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, projection, null, null, null, null));
            }
        });
    }
    
    /**
     * Method for making favorite Button Active
     */
    private void favoriteButtonActive(){
        favoriteBTN.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), android.R.drawable.btn_star_big_on));
        addToDataBase();
        showMessage(R.string.database_add);
    }
    
    /**
     * Method for making favorite Button Inactive
     */
    private void favoriteButtonInactive(){
        favoriteBTN.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), android.R.drawable.btn_star_big_off));
        deleteFromDataBase();
        showMessage(R.string.database_remove);
    }
    
    /**
     * Method for adding movie data to sqlite database via content resolver
     */
    private void addToDataBase() {
        ContentResolver contentResolver = getContentResolver();
        ContentValues values = new ContentValues();
        values.put(MovieContract.MovieEntry.COLUMN_TITLE, title);
        values.put(MovieContract.MovieEntry.COLUMN_RELEASE, release);
        values.put(MovieContract.MovieEntry.COLUMN_POSTER, posterEndpoint);
        values.put(MovieContract.MovieEntry.COLUMN_VOTE, vote);
        values.put(MovieContract.MovieEntry.COLUMN_PLOT, plot);
        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movieID);
        contentResolver.insert(MovieContract.MovieEntry.CONTENT_URI, values);
    }

     /**
     * Method for removing movie data from sqlite database via content resolver
     */
    private void deleteFromDataBase() {
        getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI, selection, null);
    }

     /**
     * Method which check if movie is in database
     */
    private boolean IsFavorite() {
        selection = MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = " + movieID;
        Cursor cursor = getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                MOVIE_LIST_PROJECTION,
                selection,
                null,
                null
        );
        if (cursor.getCount() <= 0) {
            Log.d(TAG, "IsFavorite: tbs false");
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    @Override
    public void showMessage(int messageId) {
        switch (messageId) {
            case R.string.database_remove:
                Snackbar.make(constraintLayout, R.string.network_disabled, Snackbar.LENGTH_LONG).show();
                break;
            case R.string.database_add:
                Snackbar.make(constraintLayout, R.string.network_disabled, Snackbar.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public void showErrorMessage(String errorMessage) {

    }

    @Override
    public void onItemClick(View view, int position) {
        Log.d(TAG, "onItemClick:  called");
        Video videoItem = videosList.get(position);
        Intent intent = YouTubeStandalonePlayer.createVideoIntent(this, getString(R.string.youtube_api_key), videoItem.getVideoEndpoint(), 0, true, false);
        startActivity(intent);
    }
}
