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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CancellationException;

import static com.github.duplicates.DuplicateTaskListener.MATCH_GOOD;

/**
 * Task to find duplicates.
 *
 * @author moshe.w
 */
public abstract class DuplicateFindTask<T extends DuplicateItem, VH extends DuplicateViewHolder<T>> extends DuplicateTask<T, Object, Object, List<T>> {

    private DuplicateComparator<T> comparator;
    private final List<T> items = new ArrayList<>();

    public DuplicateFindTask(Context context, DuplicateTaskListener listener) {
        super(context, listener);
    }

    public abstract DuplicateAdapter<T, VH> createAdapter();

    public abstract DuplicateComparator<T> createComparator();

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        items.clear();
        this.comparator = createComparator();
    }

    @Override
    protected List<T> doInBackground(Object... params) {
        try {
            getProvider().fetchItems();
        } catch (CancellationException e) {
        }
        return items;
    }

    @Override
    protected void onProgressUpdate(Object... progress) {
        if (progress.length == 1) {
            getListener().onDuplicateTaskProgress(this, (Integer) progress[0]);
        } else {
            T item1 = (T) progress[1];
            T item2 = (T) progress[2];
            float match = (float) progress[3];
            getListener().onDuplicateTaskMatch(this, item1, item2, match);
        }
    }

    @Override
    public void onItemFetched(DuplicateProvider<T> provider, int count, T item) {
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

    @Override
    public void onItemDeleted(DuplicateProvider<T> provider, int count, T item) {
        // Nothing to do.
    }

    @Override
    public void onPairDeleted(DuplicateProvider<T> provider, int count, DuplicateItemPair<T> pair) {
        // Nothing to do.
    }

    @Override
    protected String[] getPermissions() {
        return getProvider().getReadPermissions();
    }
}
