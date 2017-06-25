/*
 * Source file of the Remove Duplicates project.
 * Copyright (c) 2016. All Rights Reserved.
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/2.0
 *
 * Contributors can be contacted by electronic mail via the project Web pages:
 *
 * https://github.com/pnemonic78/RemoveDuplicates
 *
 * Contributor(s):
 *   Moshe Waisberg
 *
 */
package com.github.duplicates;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.android.removeduplicates.R;

import java.text.NumberFormat;

/**
 * View holder for a duplicate item.
 *
 * @author moshe.w
 */
public abstract class DuplicateViewHolder<T extends DuplicateItem> extends RecyclerView.ViewHolder {

    protected final ColorDrawable colorDifferent;
    protected final NumberFormat percentFormatter = NumberFormat.getPercentInstance();

    protected T item1;
    protected T item2;
    protected final OnItemCheckedChangeListener onCheckedChangeListener;

    public DuplicateViewHolder(View itemView) {
        this(itemView, null);
    }

    public DuplicateViewHolder(View itemView, OnItemCheckedChangeListener onCheckedChangeListener) {
        super(itemView);
        this.onCheckedChangeListener = onCheckedChangeListener;
        this.colorDifferent = new ColorDrawable(itemView.getContext().getResources().getColor(R.color.different));
    }

    /**
     * Bind the pair to the view.
     *
     * @param pair the pair of items.
     */
    public void bind(DuplicateItemPair<T> pair) {
        this.item1 = pair.getItem1();
        this.item2 = pair.getItem2();
        Context context = itemView.getContext();
        bindHeader(context, pair);
        bindItem1(context, pair.getItem1());
        bindItem2(context, pair.getItem2());
        bindDifference(context, pair);
    }

    protected abstract void bindHeader(Context context, DuplicateItemPair<T> pair);

    protected abstract void bindItem1(Context context, T item);

    protected abstract void bindItem2(Context context, T item);

    protected abstract void bindDifference(Context context, DuplicateItemPair<T> pair);

    protected void bindDifference(View view1, View view2, boolean different) {
        if (different) {
            view1.setBackgroundDrawable(colorDifferent);
            view2.setBackgroundDrawable(colorDifferent);
        } else {
            view1.setBackgroundDrawable(null);
            view2.setBackgroundDrawable(null);
        }
    }

    public interface OnItemCheckedChangeListener<T> {
        /**
         * Notification that the item was clicked.
         *
         * @param item    the item.
         * @param checked is checked?
         */
        void onItemCheckedChangeListener(T item, boolean checked);
    }
}