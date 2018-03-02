package com.example.rewan.ui.detail;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rewan.R;
import com.example.rewan.model.Movie;
import com.example.rewan.model.Video;
import com.example.rewan.recycler.MoviesAdapter;
import com.example.rewan.recycler.OnRecyclerClickListener;
import com.example.rewan.recycler.RecyclerItemClickListener;
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
    @BindView(R.id.release_tv)
    TextView releaseTV;
    @BindView(R.id.vote_tv)
    TextView voteTV;
    @BindView(R.id.plot_tv)
    TextView plotTV;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.trailers_recycler_view)
    RecyclerView recyclerView;

    String TAG = "DetailMovie";
    DetailPresenter detailPresenter;

    String title;
    String posterEndpoint;
    String release;
    String vote;
    String plot;
    String id;
    List<Video> videosList;
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
        setupRecyclerView();

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
        }
    }
    void getFromInstanceState (Bundle savedInstanceState){
        title = (String) savedInstanceState.getSerializable(detailPresenter.getTitle());
        release = (String) savedInstanceState.getSerializable(detailPresenter.getRelease());
        vote = (String) savedInstanceState.getSerializable(detailPresenter.getVote());
        plot = (String) savedInstanceState.getSerializable(detailPresenter.getPlot());
        posterEndpoint = (String) savedInstanceState.getSerializable(detailPresenter.getPoster());
        id = (String) savedInstanceState.getSerializable(detailPresenter.getID());
    }
    public void setupRetrofit() {
        Retrofit retrofit = ((RetrofitHelper) getApplication()).getRetrofitInstance();
        dataService = retrofit.create(DataService.class);
    }

    public void setView(){
        setTitle(title);
        releaseTV.setText(release);
        voteTV.setText(vote);
        plotTV.setText(plot);
        Picasso.with(this)
                .load(detailPresenter.getPosterPath(posterEndpoint))
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(posterIV);
    }
    private void setupRecyclerView() {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(DetailActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, this));
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }
//    @Override
    public void setVideosAdapter(List<Video> videos) {
        Log.d(TAG, "setVideosAdapter: called");
        videosList = videos;
        VideosAdapter videosAdapter = new VideosAdapter(videos, this);
        recyclerView.setAdapter(videosAdapter);
    }

    @Override
    public void showMessage(int messageId) {

    }

    @Override
    public void showErrorMessage(String errorMessage) {

    }

    @Override
    public void onItemClick(View view, int position) {

    }
}
