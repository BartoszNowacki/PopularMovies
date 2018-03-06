package com.example.rewan.ui.main;

import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v4.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.rewan.data.MovieContract;
import com.example.rewan.model.Movie;
import com.example.rewan.R;
import com.example.rewan.recycler.FavoriteMovieAdapter;
import com.example.rewan.recycler.MoviesAdapter;
import com.example.rewan.retrofit.DataService;
import com.example.rewan.retrofit.RetrofitHelper;
import com.example.rewan.ui.detail.DetailActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Retrofit;


public class MainActivity
        extends AppCompatActivity
        implements MainContract.View, MoviesAdapter.MovieAdapterOnClickHandler, FavoriteMovieAdapter.MovieAdapterOnClickHandler, LoaderManager.LoaderCallbacks<Cursor> {

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
    private FavoriteMovieAdapter favoriteAdapter;
    private int mPosition = RecyclerView.NO_POSITION;

    public static final String[] MOVIE_LIST_PROJECTION = {
            MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_TITLE,
            MovieContract.MovieEntry.COLUMN_RELEASE,
            MovieContract.MovieEntry.COLUMN_POSTER,
            MovieContract.MovieEntry.COLUMN_VOTE,
            MovieContract.MovieEntry.COLUMN_PLOT,
            MovieContract.MovieEntry.COLUMN_MOVIE_ID,
    };

    public static final int INDEX_ID = 0;
    public static final int INDEX_MOVIE_TITLE = 1;
    public static final int INDEX_MOVIE_RELEADE = 2;
    public static final int INDEX_MOVIE_POSTER = 3;
    public static final int INDEX_MOVIE_VOTE = 4;
    public static final int INDEX_MOVIE_PLOT = 5;
    public static final int INDEX_MOVIE_ID = 6;


    private static final int ID_MOVIE_LOADER = 1;

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

        getSupportLoaderManager().initLoader(ID_MOVIE_LOADER, null, this);
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

    /**
     * Configure spinner for Sort order
     */
    private void configureSortSpinner() {
        final MenuItem item = menu.findItem(R.id.sort);
        final String favorite = getString(R.string.favorite);
        sortSpinner = (Spinner) item.getActionView();
        ArrayAdapter<CharSequence> sortAdapter = ArrayAdapter.createFromResource(this, R.array.sort_criteria_array, android.R.layout.simple_spinner_item);
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(sortAdapter);
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = sortSpinner.getSelectedItem().toString();
                if (favorite==selectedItem){
                    setFavoriteAdapter();
                } else {
                    checkSortOrder(selectedItem);
                    mainPresenter.getMovies(MainActivity.this);
                }
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
        MoviesAdapter moviesAdapter = new MoviesAdapter(movies, this, this);
        recyclerView.setAdapter(moviesAdapter);
    }

    public void setFavoriteAdapter(){
        favoriteAdapter = new FavoriteMovieAdapter(this, this);
        recyclerView.setAdapter(favoriteAdapter);
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
        Snackbar.make(constraintLayout, R.string.error + errorMessage, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onClick(int movieId, View thumbnail, int position) {
        Intent intent = new Intent(this, DetailActivity.class);
        Pair<View, String> p1 = Pair.create(thumbnail, getString(R.string.thumbnail_transition_name));
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, p1);
        startActivity(mainPresenter.configuredIntent(intent, moviesList.get(position), position), options.toBundle());
    }


    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {
        switch (loaderId) {
            case ID_MOVIE_LOADER:
                return new CursorLoader(
                        this,
                        MovieContract.MovieEntry.CONTENT_URI,
                        MOVIE_LIST_PROJECTION,
                        null,
                        null,
                        MovieContract.MovieEntry.COLUMN_RELEASE + " DESC");
            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if (data != null) {
            favoriteAdapter.swapCursor(data);
            if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
            recyclerView.smoothScrollToPosition(mPosition);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        favoriteAdapter.swapCursor(null);
    }
}
