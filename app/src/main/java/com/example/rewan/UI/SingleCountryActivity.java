package com.example.rewan.UI;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.rewan.R;

import butterknife.BindView;
import butterknife.ButterKnife;



public class SingleCountryActivity extends AppCompatActivity {

    @BindView(R.id.nameValueTV)
    TextView nameTV;
    @BindView(R.id.alpha2ValueTV)
    TextView alpha2TV;
    @BindView(R.id.alpha3ValueTV)
    TextView alpha3TV;

    String TAG = "SingleCountry";
    public static String COUNTRY_NAME = "name";
    public static String COUNTRY_ALPHA2 = "alpha2";
    public static String COUNTRY_ALPHA3 = "alpha3";


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
            nameTV.setText(extras.getString(COUNTRY_NAME));
            alpha2TV.setText(extras.getString(COUNTRY_ALPHA2));
            alpha3TV.setText(extras.getString(COUNTRY_ALPHA3));
        }
    }

    void setView(Bundle savedInstanceState){
        nameTV.setText((String) savedInstanceState.getSerializable("name"));
        alpha2TV.setText((String) savedInstanceState.getSerializable("alpha2code"));
        alpha3TV.setText((String) savedInstanceState.getSerializable("alpha3code"));
    }

}
