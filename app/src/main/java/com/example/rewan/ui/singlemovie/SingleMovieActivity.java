package com.example.rewan.ui.singlemovie;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.rewan.R;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Class for details of Movie object
 */
public class SingleMovieActivity extends AppCompatActivity {

    @BindView(R.id.nameValueTV)
    TextView nameTV;
    @BindView(R.id.alpha2ValueTV)
    TextView alpha2TV;
    @BindView(R.id.alpha3ValueTV)
    TextView alpha3TV;

    String TAG = "SingleMovie";
    public final static String MOVIE_TITLE = "title";
    public final static String MOVIE_RELEASE = "release";
    public final static String MOVIE_POSTER = "poster";
    public final static String MOVIE_VOTE = "vote";
    public final static String MOVIE_PLOT = "plot";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_country);
        ButterKnife.bind(this);
        if (savedInstanceState == null) {
            setViewFromExtras();
        } else {
            setView(savedInstanceState);
        }
    }

    void setViewFromExtras(){
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            Log.d(TAG, "onCreate:  no extra");
        } else {
            nameTV.setText(extras.getString(MOVIE_TITLE));
            alpha2TV.setText(extras.getString(MOVIE_RELEASE));
            alpha3TV.setText(extras.getString(MOVIE_POSTER));
            alpha3TV.setText(extras.getString(MOVIE_VOTE));
            alpha3TV.setText(extras.getString(MOVIE_PLOT));
        }
    }

    void setView(Bundle savedInstanceState){
        nameTV.setText((String) savedInstanceState.getSerializable("name"));
        alpha2TV.setText((String) savedInstanceState.getSerializable("alpha2code"));
        alpha3TV.setText((String) savedInstanceState.getSerializable("alpha3code"));
    }

}
