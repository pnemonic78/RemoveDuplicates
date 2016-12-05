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
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import static com.github.duplicates.DuplicateTaskListener.MATCH_GOOD;

/**
 * Task to find duplicates.
 *
 * @author moshe.w
 */
public abstract class DuplicateFindTask<T extends DuplicateItem, VH extends DuplicateViewHolder<T>> extends AsyncTask<Object, Object, List<T>>
        implements DuplicateProviderListener<T, DuplicateProvider<T>> {

    private final Context context;
    private final DuplicateTaskListener listener;
    private DuplicateProvider<T> provider;
    private DuplicateComparator<T> comparator;
    private final List<T> items = new ArrayList<>();

    public DuplicateFindTask(Context context, DuplicateTaskListener listener) {
        this.context = context;
        this.listener = listener;
    }

    protected Context getContext() {
        return context;
    }

    protected DuplicateTaskListener getListener() {
        return listener;
    }

    protected abstract DuplicateProvider<T> createProvider(Context context);

    public abstract DuplicateAdapter<T, VH> createAdapter();

    public abstract DuplicateComparator<T> createComparator();

    @Override
    protected void onPreExecute() {
        listener.onDuplicateTaskStarted(this);
        items.clear();
        this.provider = createProvider(getContext());
        provider.setListener(this);
        this.comparator = createComparator();
    }

    @Override
    protected List<T> doInBackground(Object... params) {
        provider.fetchItems();
        return items;
    }

    @Override
    protected void onProgressUpdate(Object... progress) {
        if (progress.length == 1) {
            listener.onDuplicateTaskProgress(this, (Integer) progress[0]);
        } else {
            T item1 = (T) progress[1];
            T item2 = (T) progress[2];
            float match = (float) progress[3];
            listener.onDuplicateTaskMatch(this, item1, item2, match);
        }
    }

    @Override
    protected void onPostExecute(List<T> result) {
        listener.onDuplicateTaskFinished(this);
    }

    @Override
    protected void onCancelled() {
        listener.onDuplicateTaskCancelled(this);
    }

    @Override
    public void onItemFetched(DuplicateProvider<T> provider, T item) {
        int size = items.size();

        // Is it a duplicate?
        float bestMatch = 0f;
        T bestItem = null;
        float match;
        T item1;
        // Most likely that a matching item is a neighbour,so count backwards.
        for (int i = size - 1; i >= 0; i--) {
            item1 = items.get(i);
            match = comparator.match(item1, item);
            if ((match > MATCH_GOOD) && (match > bestMatch)) {
                bestMatch = match;
                bestItem = item1;
                if (match == 1f) {
                    break;
                }
            }
        }
        if (bestItem != null) {
            publishProgress(size, bestItem, item, bestMatch);
        }

        if (items.add(item)) {
            publishProgress(size + 1);
        }
    }
}
