package com.graduation.android.readme.base.adapter.listener;

import android.support.v7.widget.RecyclerView;

/**
 *
 * @author linyt
 * @Email lytjackson@gmail.com
 * @date 2017/7/18 0018
 */
public interface OnItemDragListener {
    void onItemDragStart(RecyclerView.ViewHolder viewHolder, int pos);

    void onItemDragMoving(RecyclerView.ViewHolder source, int from, RecyclerView.ViewHolder target, int to);

    void onItemDragEnd(RecyclerView.ViewHolder viewHolder, int pos);

}
