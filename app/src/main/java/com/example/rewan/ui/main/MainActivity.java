package com.example.rewan.ui.main;

import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import com.example.rewan.ui.BaseActivity;
import com.example.rewan.ui.detail.DetailActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Retrofit;


public class MainActivity
        extends BaseActivity
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
    GridLayoutManager layoutManager;
    private FavoriteMovieAdapter favoriteAdapter;

    private int mPosition = RecyclerView.NO_POSITION;
    private int sortOrderID;
    int currentVisiblePosition;
    private boolean initialized;



    public static final String SORT_TYPE = "sort type";
    private static final int ID_MOVIE_LOADER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        setupRecyclerView();
        setupRetrofit();

        initialized=false;
        sortOrderID = 0;
        currentVisiblePosition = 0;

        favoriteAdapter = new FavoriteMovieAdapter(this, this);
        String apiKey = getString(R.string.movie_api_key);
        mainPresenter = new MainPresenter(dataService, apiKey);
        mainPresenter.attachView(this);

        getSupportLoaderManager().initLoader(ID_MOVIE_LOADER, null, this);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            sortOrderID = savedInstanceState.getInt(SORT_TYPE, 0);
            currentVisiblePosition = savedInstanceState.getInt(POSITION, 0);
        }
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
        currentVisiblePosition = ((GridLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putInt(SORT_TYPE, sortSpinner.getSelectedItemPosition());
        state.putInt(POSITION, currentVisiblePosition);
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
        sortSpinner = (Spinner) item.getActionView();
        ArrayAdapter<CharSequence> sortAdapter = ArrayAdapter.createFromResource(this, R.array.sort_criteria_array, android.R.layout.simple_spinner_item);
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(sortAdapter);
        sortSpinner.setSelection(sortOrderID);
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sortOrderID = sortSpinner.getSelectedItemPosition();
                makeDemand();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void makeDemand(){
        final int popularID = 0;
        final int topID = 1;
        final int favoriteID = 2;
        switch (sortOrderID) {
            case popularID:
                mainPresenter.makeFavorite(false);
                checkSortOrder(getString(R.string.popular));
                mainPresenter.getMovies(MainActivity.this);
                break;
            case topID:
                mainPresenter.makeFavorite(false);
                checkSortOrder(getString(R.string.top_rated));
                mainPresenter.getMovies(MainActivity.this);
                break;
            case favoriteID:
                mainPresenter.makeFavorite(true);
                recyclerView.setAdapter(favoriteAdapter);
                goToScrollPosition();
                break;
        }
    }

    public void goToScrollPosition(){
        recyclerView.scrollToPosition(currentVisiblePosition);
        currentVisiblePosition = 0;
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
        int span = 2;
        if(isLandscapeMode()){
            span=3;
        }
        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(MainActivity.this, span);
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

    @Override
    public void showMessage(int messageId) {
        switch (messageId) {
            case R.string.network_disabled:
                Snackbar.make(constraintLayout, R.string.network_disabled, Snackbar.LENGTH_LONG).show();
                break;
            case R.string.network_available:
                if(initialized) {
                    Snackbar.make(constraintLayout, R.string.network_available, Snackbar.LENGTH_LONG)
                            .setAction(R.string.refresh, new refreshData())
                            .show();
                }
                initialized = true;
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
        startActivity(mainPresenter.configuredIntent(intent, moviesList.get(position), position));
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
                        null);
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

    @Override
    public void onFavoriteClick(String movieId, View thumbnail, int position) {
        Intent intent = new Intent(this, DetailActivity.class);
        Cursor cursor = makeSingleMovieQuery(movieId);
        cursor.moveToFirst();
        intent.putExtra(Movie.MovieTags.MOVIE_TITLE, cursor.getString(INDEX_MOVIE_TITLE));
        intent.putExtra(Movie.MovieTags.MOVIE_RELEASE, cursor.getString(INDEX_MOVIE_RELEASE));
        intent.putExtra(Movie.MovieTags.MOVIE_PLOT, cursor.getString(INDEX_MOVIE_PLOT));
        intent.putExtra(Movie.MovieTags.MOVIE_POSTER, cursor.getString(INDEX_MOVIE_POSTER));
        intent.putExtra(Movie.MovieTags.MOVIE_BACKDROP, cursor.getString(INDEX_MOVIE_BACKDROP));
        intent.putExtra(Movie.MovieTags.MOVIE_VOTE, cursor.getString(INDEX_MOVIE_VOTE));
        intent.putExtra(Movie.MovieTags.ID, cursor.getString(INDEX_MOVIE_ID));
        startActivity(intent);
    }
    /**
     * Method to check if screen is in Landscape mode
     * @param movieID
     * @return Cursor
     */
    private Cursor makeSingleMovieQuery (String movieID){
        String selection = MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = " + movieID;
        return  getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                MOVIE_LIST_PROJECTION,
                selection,
                null,
                null);
    }

    private class refreshData implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            makeDemand();
        }
    }
}