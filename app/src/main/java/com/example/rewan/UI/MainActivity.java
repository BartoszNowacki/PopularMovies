package com.example.rewan.UI;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.rewan.Model.Country;
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


public class MainActivity extends AppCompatActivity implements OnRecyclerClickListener {

    @BindView(R.id.countries_recycler_view)
    RecyclerView recyclerView;

    private CountriesAdapter countriesAdapter;
    private LinearLayoutManager layoutManager;

    String TAG = "Main Activity";
    List<Country> countriesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setupRecyclerView();
        setupRetrofit();
    }
    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(this, SingleCountryActivity.class);
        Country country = countriesList.get(position);
        intent.putExtra("name", country.getName());
        intent.putExtra("alpha2code", country.getAlpha2_code());
        intent.putExtra("alpha3code", country.getAlpha3_code());
        startActivity(intent);
    }

    private void setupRecyclerView(){
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, this));
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public void setupRetrofit(){
        Retrofit retrofit = ((RetrofitHelper)getApplication()).getRetrofitInstance();
        DataService dataService = retrofit.create(DataService.class);
        Call<JsonObject> countryCall = dataService.loadCountries();
        countryCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    List<Country> countriesList = convertResponse(response.body());
                    setCountriesAdapter(countriesList);
                } else {
                    int httpCode = response.code();
                    Log.d(TAG, "onResponse: " + httpCode);
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });

    }

    private List<Country> convertResponse(JsonObject restResponse){

        JsonObject result = restResponse.getAsJsonObject("RestResponse");
        JsonArray countriesObject = result.getAsJsonArray("result");
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Country>>(){}.getType();

        return (List<Country>) gson.fromJson(countriesObject, listType);

    }
    private void setCountriesAdapter(List<Country> countries) {
        countriesList = countries;
        countriesAdapter = new CountriesAdapter(MainActivity.this, countriesList);
        recyclerView.setAdapter(countriesAdapter);
    }

}
