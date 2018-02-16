package com.example.rewan.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Created by Rewan on 15.02.2018.
 */

public class Country {

        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("alpha2_code")
        @Expose
        private String alpha2_code;
        @SerializedName("alpha3_code")
        @Expose
        private String alpha3_code;



        public String getName() {
            return name;
        }

        public String getAlpha2_code() {
            return alpha2_code;
        }


        public String getAlpha3_code() {
            return alpha3_code;
        }


        @Override
        public String toString() {
            return "Country{" +
                    "name='" + name + '\'' +
                    ", alpha2_code='" + alpha2_code + '\'' +
                    ", alpha3_code='" + alpha3_code + '\'' +
                    '}';
        }



}
