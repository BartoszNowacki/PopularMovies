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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_country);
        ButterKnife.bind(this);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                Log.d(TAG, "onCreate:  no extra");
            } else {
                nameTV.setText(extras.getString("name"));
                alpha2TV.setText(extras.getString("alpha2code"));
                alpha3TV.setText(extras.getString("alpha3code"));
            }
        } else {
            nameTV.setText((String) savedInstanceState.getSerializable("name"));
            alpha2TV.setText((String) savedInstanceState.getSerializable("alpha2code"));
            alpha3TV.setText((String) savedInstanceState.getSerializable("alpha3code"));
        }
    }

}
