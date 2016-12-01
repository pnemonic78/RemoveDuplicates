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

import java.util.List;

/**
 * Task to find duplicates.
 *
 * @author moshe.w
 */
public abstract class DuplicateTask<T extends DuplicateItem, VH extends DuplicateViewHolder<T>> extends AsyncTask<Object, Object, List<T>> {

    private final Context context;
    private final DuplicateTaskListener listener;
    private DuplicateComparator<T> comparator;

    public DuplicateTask(Context context, DuplicateTaskListener listener) {
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
    }

    @Override
    protected List<T> doInBackground(Object... params) {
        DuplicateProvider<T> provider = createProvider(getContext());
        this.comparator = createComparator();
        List<T> items = provider.getItems();
        //TODO find duplicates.
        int size = Math.min(items.size(), 100) - 1;
        for (int i = 0; i < size; i += 2) {
            publishProgress(i + 1, items.get(i), items.get(i + 1));
        }
        return items;
    }

    protected void onProgressUpdate(Object... progress) {
        listener.onDuplicateTaskProgress(this, (Integer) progress[0]);

        T item1 = (T) progress[1];
        T item2 = (T) progress[2];
        float match = comparator.match(item1, item2);
        if (match > 0.8) {
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
}
