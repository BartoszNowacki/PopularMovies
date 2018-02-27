package com.example.rewan.ui.main;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.rewan.model.Movie;
import com.example.rewan.R;
import com.example.rewan.recycler.MoviesAdapter;
import com.example.rewan.recycler.OnRecyclerClickListener;
import com.example.rewan.recycler.RecyclerItemClickListener;
import com.example.rewan.retrofit.DataService;
import com.example.rewan.retrofit.RetrofitHelper;
import com.example.rewan.ui.detail.DetailActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Retrofit;


public class MainActivity
        extends AppCompatActivity
        implements MainContract.View, OnRecyclerClickListener {

    @BindView(R.id.countries_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.constraint_layout)
    ConstraintLayout constraintLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    Spinner sortSpinner;
    private Menu menu;

    List<Movie> moviesList;
    private DataService dataService;
    MainPresenter mainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        setupRecyclerView();
        setupRetrofit();
        String apiKey = getString(R.string.movie_api_key);
        mainPresenter = new MainPresenter(dataService, apiKey);
        mainPresenter.attachView(this);
        mainPresenter.getMovies(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mainPresenter.getReceiver(), mainPresenter.getIntentFilter());
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mainPresenter.getReceiver());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.main, menu);
        configureSortSpinner();
        return true;
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(this, DetailActivity.class);
        startActivity(mainPresenter.configuredIntent(intent, moviesList.get(position)));
    }

    /**
     * Configure spinner for Sort order
     */
    private void configureSortSpinner() {
        final MenuItem item = menu.findItem(R.id.sort);
        sortSpinner = (Spinner) item.getActionView();
        ArrayAdapter<CharSequence> sortAdapter = ArrayAdapter.createFromResource(this, R.array.sort_criteria_array, android.R.layout.simple_spinner_item);
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(sortAdapter);
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = sortSpinner.getSelectedItem().toString();
                checkSortOrder(selectedItem);
                mainPresenter.getMovies(MainActivity.this);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    /**
     * Checks sort Order category and send it to mainPresenter.
     */
    private void checkSortOrder(String sortType) {
        if (sortType.equals(getString(R.string.popular))) {
            mainPresenter.setTopCategory(false);
        } else if (sortType.equals(getString(R.string.top_rated))) {
            mainPresenter.setTopCategory(true);
        }
    }

    /**
     * Method to setup RecyclerView for MainActivity
     */
    private void setupRecyclerView() {
        recyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(MainActivity.this, 2);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, this));
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    /**
     * Method to setup Retrofit instance
     */
    public void setupRetrofit() {
        Retrofit retrofit = ((RetrofitHelper) getApplication()).getRetrofitInstance();
        dataService = retrofit.create(DataService.class);
    }

    /**
     * Method to set RecyclerView adapter
     *
     * @param movies List of Movie objects
     */
    @Override
    public void setMoviesAdapter(List<Movie> movies) {
        moviesList = movies;
        MoviesAdapter moviesAdapter = new MoviesAdapter(movies, this);
        recyclerView.setAdapter(moviesAdapter);
    }

    @Override
    public void showMessage(int messageId) {
        switch (messageId) {
            case R.string.network_disabled:
                Snackbar.make(constraintLayout, R.string.network_disabled, Snackbar.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public void showErrorMessage(String errorMessage) {
        Snackbar.make(constraintLayout, "Error with code: " + errorMessage, Snackbar.LENGTH_LONG).show();
    }
}
