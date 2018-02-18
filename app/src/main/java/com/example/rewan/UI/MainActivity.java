package com.example.rewan.UI;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.rewan.Model.Country;
import com.example.rewan.Network.NetworkHelper;
import com.example.rewan.Network.NetworkStateDataListener;
import com.example.rewan.R;
import com.example.rewan.RecyclerView.CountriesAdapter;
import com.example.rewan.RecyclerView.OnRecyclerClickListener;
import com.example.rewan.RecyclerView.RecyclerItemClickListener;
import com.example.rewan.Retrofit.DataService;
import com.example.rewan.Retrofit.RetrofitHelper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

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

    private Call<JsonObject> countryCall;
    List<Country> countriesList;
    NetworkHelper networkHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        networkHelper = new NetworkHelper(this);
        setupRecyclerView();
        setupRetrofit();
        if (networkHelper.isNetworkAvailable(this)){
            makeCountryCall();
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
        Intent intent = new Intent(this, SingleCountryActivity.class);
        Country country = countriesList.get(position);
        intent.putExtra(SingleCountryActivity.COUNTRY_NAME, country.getName());
        intent.putExtra(SingleCountryActivity.COUNTRY_ALPHA2, country.getAlpha2_code());
        intent.putExtra(SingleCountryActivity.COUNTRY_ALPHA3, country.getAlpha3_code());
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
        DataService dataService = retrofit.create(DataService.class);
        countryCall = dataService.loadCountries();
    }
    /**
     * Method to GET response from server
     */
    @Override
    public void makeCountryCall(){
        countryCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    List<Country> countriesList = convertResponse(response.body());
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
     * Method to convert Json object returned from server to List<Country>
     * @param restResponse JsonObject returned from server
     * @return List<Country> List of Country objects
     */
    private List<Country> convertResponse(JsonObject restResponse){

        JsonObject result = restResponse.getAsJsonObject("RestResponse");
        JsonArray countriesObject = result.getAsJsonArray("result");
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Country>>(){}.getType();

        return (List<Country>) gson.fromJson(countriesObject, listType);
    }
    /**
     * Method to set RecyclerView adapter
     * @param countries List of Country objects
     */
    private void setCountriesAdapter(List<Country> countries) {
        countriesList = countries;
        CountriesAdapter countriesAdapter = new CountriesAdapter(countries);
        recyclerView.setAdapter(countriesAdapter);
    }
}
