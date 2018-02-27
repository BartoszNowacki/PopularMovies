package com.example.rewan.ui.detail;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rewan.R;
import com.example.rewan.ui.main.MainContract;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Class for details of Movie object
 */
public class DetailActivity
        extends AppCompatActivity
        implements DetailContract.View {

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

    String TAG = "SingleMovie";
    DetailPresenter detailPresenter;

    String title;
    String posterEndpoint;
    String release;
    String vote;
    String plot;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        detailPresenter = new DetailPresenter();
        detailPresenter.attachView(this);

        if (savedInstanceState == null) {
            getFromExtras();
        } else {
            getFromInstanceState(savedInstanceState);
        }
        setView();
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
        }
    }
    void getFromInstanceState (Bundle savedInstanceState){
        title = (String) savedInstanceState.getSerializable(detailPresenter.getTitle());
        release = (String) savedInstanceState.getSerializable(detailPresenter.getRelease());
        vote = (String) savedInstanceState.getSerializable(detailPresenter.getVote());
        plot = (String) savedInstanceState.getSerializable(detailPresenter.getPlot());
        posterEndpoint = (String) savedInstanceState.getSerializable(detailPresenter.getPoster());
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

    @Override
    public void showMessage(int messageId) {

    }

    @Override
    public void showErrorMessage(String errorMessage) {

    }
}
