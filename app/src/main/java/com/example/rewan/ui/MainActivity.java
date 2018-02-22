package com.example.rewan.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.rewan.model.Movie;
import com.example.rewan.network.NetworkHelper;
import com.example.rewan.network.NetworkStateDataListener;
import com.example.rewan.R;
import com.example.rewan.recycler.MoviesAdapter;
import com.example.rewan.recycler.OnRecyclerClickListener;
import com.example.rewan.recycler.RecyclerItemClickListener;
import com.example.rewan.retrofit.DataService;
import com.example.rewan.retrofit.RetrofitHelper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class MainActivity extends AppCompatActivity implements OnRecyclerClickListener, NetworkStateDataListener {

    @BindView(R.id.countries_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.constraint_layout)
    ConstraintLayout constraintLayout;

    List<Movie> countriesList;
    NetworkHelper networkHelper;
    private DataService dataService;
    private boolean isTopCategory=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        networkHelper = new NetworkHelper(this);
        setupRecyclerView();
        setupRetrofit();
        if (networkHelper.isNetworkAvailable(this)){
            makeMovieCall();
        }else {
            Snackbar.make(constraintLayout, "There is no network connection", Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(networkHelper, networkHelper.getIntentFilter());
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(networkHelper);
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(this, SingleMovieActivity.class);
        Movie movie = countriesList.get(position);
        intent.putExtra(SingleMovieActivity.MOVIE_TITLE, movie.getTitle());
        intent.putExtra(SingleMovieActivity.MOVIE_RELEASE, movie.getReleaseDate());
        intent.putExtra(SingleMovieActivity.MOVIE_PLOT, movie.getPlotSynopsis());
        intent.putExtra(SingleMovieActivity.MOVIE_POSTER, movie.getMoviePoster());
        intent.putExtra(SingleMovieActivity.MOVIE_VOTE, movie.getVoteAverage());
        startActivity(intent);
    }
    /**
     * Method to setup RecyclerView for MainActivity
     */
    private void setupRecyclerView(){
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, this));
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }
    /**
     * Method to setup Retrofit instance
     */
    public void setupRetrofit(){
        Retrofit retrofit = ((RetrofitHelper)getApplication()).getRetrofitInstance();
        dataService = retrofit.create(DataService.class);
    }
    /**
     * Method to GET response from server
     */
    @Override
    public void makeMovieCall(){
        Call<JsonObject> moviesCall = getSortOrder();
        moviesCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    List<Movie> countriesList = convertResponse(response.body());
                    setCountriesAdapter(countriesList);
                } else {
                    int httpCode = response.code();
                    Snackbar.make(constraintLayout, "Error with http code" + httpCode, Snackbar.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                Snackbar.make(constraintLayout, "Error with code" + t.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });
    }
    /**
     * Method to return proper GET method for sort order category
     */
    public Call<JsonObject> getSortOrder(){
        if (isTopCategory){
            return dataService.loadPopularMovies(getString(R.string.movie_api_key));
        }else{
            return dataService.loadTopMovies(getString(R.string.movie_api_key));
        }
    }

    /**
     * Method to convert Json object returned from server to List<Movie>
     * @param restResponse JsonObject returned from server
     * @return List<Movie> List of Movie objects
     */
    private List<Movie> convertResponse(JsonObject restResponse){
        Log.d("tbs", "convertResponse: " + restResponse);
        JsonArray moviesJsonArray = restResponse.getAsJsonArray("results");
        Log.d("tbs", "convertResponse: " + moviesJsonArray);
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Movie>>(){}.getType();

        return (List<Movie>) gson.fromJson(moviesJsonArray, listType);
    }
    /**
     * Method to set RecyclerView adapter
     * @param countries List of Movie objects
     */
    private void setCountriesAdapter(List<Movie> countries) {
        countriesList = countries;
        MoviesAdapter moviesAdapter = new MoviesAdapter(countries, this);
        recyclerView.setAdapter(moviesAdapter);
    }
}
