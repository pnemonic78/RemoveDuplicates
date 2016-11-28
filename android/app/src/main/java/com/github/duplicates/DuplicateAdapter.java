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
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * List adapter for duplicate items.
 *
 * @author moshe.w
 */
public class DuplicateAdapter<VH extends DuplicateViewHolder, T extends DuplicateItem> extends RecyclerView.Adapter<VH> {

    private final List<T> items = new ArrayList<>();

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        T item = items.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     * Clear the items.
     */
    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    /**
     * Add an item.
     *
     * @param item the item.
     */
    public void add(T item) {
        items.add(item);
        notifyDataSetChanged();
    }

    /**
     * Add items.
     *
     * @param items the list of items.
     */
    public void addAll(Collection<T> items) {
        this.items.addAll(items);
        notifyDataSetChanged();
    }
}
