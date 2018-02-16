package com.example.rewan.Network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.util.Log;


import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class NetworkHelper {


    static String TAG = "Network Helper";
    public static OkHttpClient.Builder getBuilder(Context context, Cache cache) {
        return new OkHttpClient.Builder()
                .cache(cache);
    }

}