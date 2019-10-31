/*
 * Copyright 2016, Moshe Waisberg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.duplicates;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.github.android.removeduplicates.R;

import java.text.NumberFormat;

/**
 * View holder for a duplicate item.
 *
 * @author moshe.w
 */
public abstract class DuplicateViewHolder<T extends DuplicateItem> extends RecyclerView.ViewHolder {

    protected final int colorDifferent;
    protected final int colorError;
    protected final NumberFormat percentFormatter = NumberFormat.getPercentInstance();

    protected T item1;
    protected T item2;
    protected final OnItemCheckedChangeListener<T> onCheckedChangeListener;

    public DuplicateViewHolder(View itemView) {
        this(itemView, null);
    }

    public DuplicateViewHolder(View itemView, OnItemCheckedChangeListener<T> onCheckedChangeListener) {
        super(itemView);
        Context context = itemView.getContext();
        this.onCheckedChangeListener = onCheckedChangeListener;
        this.colorDifferent = context.getResources().getColor(R.color.different);
        this.colorError = context.getResources().getColor(R.color.error);
    }

    /**
     * Bind the pair to the view.
     *
     * @param pair the pair of items.
     */
    public void bind(DuplicateItemPair<T> pair) {
        this.item1 = pair.item1;
        this.item2 = pair.item2;
        Context context = itemView.getContext();
        bindHeader(context, pair);
        bindItem1(context, item1);
        bindItem2(context, item2);
        bindDifference(context, pair);

        if (item1.isError || item2.isError) {
            itemView.setBackgroundColor(colorError);
        } else {
            itemView.setBackground(null);
        }
    }

    protected abstract void bindHeader(Context context, DuplicateItemPair<T> pair);

    protected abstract void bindItem1(Context context, T item);

    protected abstract void bindItem2(Context context, T item);

    protected abstract void bindDifference(Context context, DuplicateItemPair<T> pair);

    protected void bindDifference(View view1, View view2, boolean different) {
        if (different) {
            view1.setBackgroundColor(colorDifferent);
            view2.setBackgroundColor(colorDifferent);
        } else {
            view1.setBackgroundColor(Color.TRANSPARENT);
            view2.setBackgroundColor(Color.TRANSPARENT);
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