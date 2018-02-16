package com.example.rewan.RecyclerView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.rewan.Model.Country;
import com.example.rewan.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Adapter for RecyclerView in Main Activity
 * @param
 */
public class CountriesAdapter extends RecyclerView.Adapter<CountriesAdapter.CountriesViewHolder>{

        private List<Country> countriesList;
        private Context mContext;

        public CountriesAdapter(Context context, List<Country> countriesList) {
            this.mContext = context;
            this.countriesList = countriesList;
        }

        @Override
        public CountriesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.country_entry, parent, false);
            return new CountriesViewHolder(view);
        }

        @Override
        public void onBindViewHolder(CountriesViewHolder holder, int position) {
            Country countryEntry = countriesList.get(position);
                holder.name.setText(countryEntry.getName());
        }

        @Override
        public int getItemCount() {
            return ((countriesList != null) && (countriesList.size() != 0) ? countriesList.size() : 1);
        }

    static class CountriesViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.country_name)
        TextView name;

        CountriesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
