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

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * List adapter for duplicate pairs.
 *
 * @author moshe.w
 */
public abstract class DuplicateAdapter<T extends DuplicateItem, VH extends DuplicateViewHolder<T>> extends RecyclerView.Adapter<VH> {

    private final List<DuplicateItemPair<T>> pairs = new ArrayList<>();

    @Override
    public void onBindViewHolder(VH holder, int position) {
        DuplicateItemPair<T> pair = pairs.get(position);
        holder.bind(pair);
    }

    @Override
    public int getItemCount() {
        return pairs.size();
    }

    /**
     * Clear the pairs.
     */
    public void clear() {
        pairs.clear();
        notifyDataSetChanged();
    }

    /**
     * Add a pair.
     *
     * @param item1 the first item.
     * @param item2 the second item.
     */
    public void add(T item1, T item2) {
        add(item1, item2, 1f);
    }

    /**
     * Add a pair.
     *
     * @param item1 the first item.
     * @param item2 the second item.
     * @param match the matching percentage.
     */
    public void add(T item1, T item2, float match) {
        int position = pairs.size();
        if (!item1.isChecked() && !item2.isChecked()) {
            DuplicateItemPair<T> pair = new DuplicateItemPair<>(item1, item2, match);
            if (pairs.add(pair)) {
                notifyItemInserted(position);
            }
        }
    }
}
