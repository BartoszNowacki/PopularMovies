package com.example.rewan.network;


import com.example.rewan.utils.CallType;

/**
 * Listener interface for Network state change
 */
public interface NetworkStateDataListener {
    /**
     * Callback method from NetworkHelper called after Network state change for connected.
     */
    void makeCall();
}
