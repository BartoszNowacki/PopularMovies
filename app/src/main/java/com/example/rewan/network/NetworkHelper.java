package com.example.rewan.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import okhttp3.Cache;
import okhttp3.OkHttpClient;

/**
 * Helper class for working with a remote server
 */
public class NetworkHelper extends BroadcastReceiver {

    IntentFilter mNetworkIntentFilter;
    NetworkStateDataListener networkStateDataListener;


    public NetworkHelper(NetworkStateDataListener dataListner) {
        super();
        mNetworkIntentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        this.networkStateDataListener = dataListner;
    }

    public IntentFilter getIntentFilter(){
        return mNetworkIntentFilter;
    }

    /**
     * Builds client which should be used with any http requests.
     * @param cache cache dir
     * @return OkHttpClient
     */
    public static OkHttpClient.Builder getBuilder(Cache cache) {
        return new OkHttpClient.Builder()
                .cache(cache);
    }
    /**
     * Method to check internet connection
     * @param context app context
     * @return boolean is connected to Network
     */
    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return (networkInfo != null && networkInfo.isConnectedOrConnecting());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (isNetworkAvailable(context)) {
                networkStateDataListener.makeMovieCall();
        }
    }
}
