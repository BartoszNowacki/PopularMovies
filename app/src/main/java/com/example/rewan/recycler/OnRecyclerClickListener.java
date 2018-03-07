package com.example.rewan.recycler;

import android.view.View;

/**
 * Listener interface for RecyclerView onClick event in DetailActivity. It is used by Videos Adapter
 */
public interface OnRecyclerClickListener {
    void onItemClick(View view, int position);
}
