package com.example.rewan.ui.detail;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
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
        extends AppCompatActivity
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

    String TAG = "DetailMovie";
    DetailPresenter detailPresenter;

    String title;
    String posterEndpoint;
    String release;
    String vote;
    String plot;
    String id;
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
        detailPresenter.setID(id);
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

    void getFromExtras(){
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            Log.d(TAG, "onCreate:  no extra");
        } else {
            title = extras.getString(detailPresenter.getTitle());
            release = extras.getString(detailPresenter.getRelease());
            vote = extras.getString(detailPresenter.getVote());
            plot = extras.getString(detailPresenter.getPlot());
            posterEndpoint = extras.getString(detailPresenter.getPoster());
            id = extras.getString(detailPresenter.getID());
            posterIV.setTransitionName(extras.getString(detailPresenter.getTransition()));
        }
    }
    void getFromInstanceState (Bundle savedInstanceState){
        title = (String) savedInstanceState.getSerializable(detailPresenter.getTitle());
        release = (String) savedInstanceState.getSerializable(detailPresenter.getRelease());
        vote = (String) savedInstanceState.getSerializable(detailPresenter.getVote());
        plot = (String) savedInstanceState.getSerializable(detailPresenter.getPlot());
        posterEndpoint = (String) savedInstanceState.getSerializable(detailPresenter.getPoster());
        id = (String) savedInstanceState.getSerializable(detailPresenter.getID());
        posterIV.setTransitionName((String) savedInstanceState.getSerializable(detailPresenter.getID()));
    }
    public void setupRetrofit() {
        Retrofit retrofit = ((RetrofitHelper) getApplication()).getRetrofitInstance();
        dataService = retrofit.create(DataService.class);
    }

    public void setView(){
        setTitle(title);
        titleTV.setText(title);
        releaseTV.setText(detailPresenter.getReleaseYear(release));
        voteTV.setText(vote);
        ratingBar.setRating(detailPresenter.convertToFloat(vote));
        plotTV.setText(plot);
        Picasso.with(this)
                .load(detailPresenter.getPosterPath(posterEndpoint))
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(posterIV);
    }
    private void setupVideosRecyclerView() {
        videosRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(DetailActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        videosRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, videosRecyclerView, this));
        videosRecyclerView.setLayoutManager(layoutManager);
        videosRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }
//    @Override
    public void setVideosAdapter(List<Video> videos) {
        Log.d(TAG, "setVideosAdapter: called");
        videosList = videos;
        if (!videosList.isEmpty()) {
            VideosAdapter videosAdapter = new VideosAdapter(videos, this);
            videosRecyclerView.setAdapter(videosAdapter);
        }
    }
    public void setReviewsAdapter(List<Review> reviews) {
        Log.d(TAG, "setReviewAdapter: called" +reviews);
        reviewsList = reviews;
        if(!reviewsList.isEmpty()) {
            ReviewsAdapter reviewsAdapter = new ReviewsAdapter(reviews);
            reviewsRecyclerView.setAdapter(reviewsAdapter);
        }
    }
    private void setupReviewsRecyclerView() {
        reviewsRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(DetailActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        reviewsRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, reviewsRecyclerView, this));
        reviewsRecyclerView.setLayoutManager(layoutManager);
        reviewsRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void showMessage(int messageId) {

    }

    @Override
    public void showErrorMessage(String errorMessage) {

    }

    @Override
    public void onItemClick(View view, int position) {
        Video videoItem = videosList.get(position);
        Intent intent = YouTubeStandalonePlayer.createVideoIntent(this, getString(R.string.youtube_api_key), videoItem.getVideoEndpoint(), 0, true, false);
startActivity(intent);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
