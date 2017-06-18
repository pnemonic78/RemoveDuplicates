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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * List adapter for duplicate pairs.
 *
 * @author moshe.w
 */
public abstract class DuplicateAdapter<T extends DuplicateItem, VH extends DuplicateViewHolder<T>> extends RecyclerView.Adapter<VH> implements DuplicateViewHolder.OnItemCheckedChangeListener<T> {

    private final List<DuplicateItemPair<T>> pairs = new ArrayList<>();

    public DuplicateAdapter() {
        setHasStableIds(true);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        DuplicateItemPair<T> pair = pairs.get(position);
        holder.bind(pair);
    }

    @Override
    public int getItemCount() {
        return pairs.size();
    }

    @Override
    public long getItemId(int position) {
        DuplicateItemPair<T> pair = pairs.get(position);
        return pair.getId();
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
        add(item1, item2, 1f, null);
    }

    /**
     * Add a pair.
     *
     * @param item1      the first item.
     * @param item2      the second item.
     * @param match      the matching percentage.
     * @param difference the array of differences.
     */
    public void add(T item1, T item2, float match, boolean[] difference) {
        if (item1.isChecked() && item2.isChecked()) {
            return;
        }
        DuplicateItemPair<T> pair = new DuplicateItemPair<>(item1, item2, match, difference);
        if (pairs.add(pair)) {
            notifyItemInserted(pairs.size());
        }
    }

    /**
     * Remove an item and its pair.
     *
     * @param item the item.
     */
    public void remove(T item) {
        List<Integer> positions = findPairs(item);
        if ((positions != null) && !positions.isEmpty()) {
            for (int position : positions) {
                pairs.remove(position);
                notifyItemRemoved(position);
            }
        }
    }

    /**
     * Remove a pair.
     *
     * @param pair the item pair.
     */
    public void remove(DuplicateItemPair<T> pair) {
        int position = pairs.indexOf(pair);
        if (position >= 0) {
            pairs.remove(position);
            notifyItemRemoved(position);
        }
    }

    /**
     * Find the pairs containing the item.
     *
     * @param item the item to find.
     * @return the list of indexes/positions - {@code null} otherwise.
     */
    protected List<Integer> findPairs(T item) {
        List<Integer> positions = new ArrayList<>();
        int size = pairs.size();
        DuplicateItemPair<T> pair;
        for (int i = 0; i < size; i++) {
            pair = pairs.get(i);
            if ((pair.getItem1() == item) || (pair.getItem2() == item)) {
                positions.add(i);
            }
        }
        return positions;
    }

    /**
     * Mark all the items as checked.
     */
    public void selectAll() {
        for (DuplicateItemPair<T> pair : pairs) {
            pair.getItem1().setChecked(true);
            pair.getItem2().setChecked(true);
        }
        notifyDataSetChanged();
    }

    /**
     * Get the list of items that are checked for deletion.
     *
     * @return the array of items.
     */
    public T[] getCheckedItems() {
        Set<T> items = new TreeSet<>();
        T item = null;
        for (DuplicateItemPair<T> pair : pairs) {
            item = pair.getItem1();
            if (item.isChecked()) {
                items.add(item);
            }
            item = pair.getItem2();
            if (item.isChecked()) {
                items.add(item);
            }
        }
        if (item == null) {
            return null;
        }
        T[] array = (T[]) Array.newInstance(item.getClass(), items.size());
        return items.toArray(array);
    }

    /**
     * Get the list of pairs that have checked items for deletion.
     *
     * @return the array of items.
     */
    public DuplicateItemPair<T>[] getCheckedPairs() {
        List<DuplicateItemPair<T>> checked = new ArrayList<>(pairs.size());
        for (DuplicateItemPair<T> pair : pairs) {
            if (pair.getItem1().isChecked() || pair.getItem2().isChecked()) {
                checked.add(pair);
            }
        }
        if (checked.isEmpty()) {
            return null;
        }
        DuplicateItemPair[] array = new DuplicateItemPair[checked.size()];
        return checked.toArray(array);
    }

    @Override
    public void onItemCheckedChangeListener(T item, boolean checked) {
        item.setChecked(checked);
        notifyDataSetChanged();//FIXME Update only the affected rows!
    }
}
