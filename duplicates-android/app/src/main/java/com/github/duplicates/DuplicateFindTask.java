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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CancellationException;

/**
 * Task to find duplicates.
 *
 * @author moshe.w
 */
public abstract class DuplicateFindTask<T extends DuplicateItem, VH extends DuplicateViewHolder<T>> extends DuplicateTask<T, Object, Object, List<T>> {

    /**
     * Percentage for two items to be considered a good match.
     */
    public static final float MATCH_GOOD = 0.71f;

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
            getProvider().fetchItems(this);
        } catch (CancellationException ignore) {
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
            boolean[] difference = (boolean[]) progress[4];
            getListener().onDuplicateTaskMatch(this, item1, item2, match, difference);
        }
    }

    @Override
    public void onItemFetched(DuplicateProvider<T> provider, int count, T item) {
        final int size = items.size();

        // Maybe the item already exists in the list?
        T item1;
        for (int i = size - 1; i >= 0; i--) {
            item1 = items.get(i);
            if (item == item1) {
                return;
            }
        }

        // Is it a duplicate?
        float bestMatch = 0f;
        T bestItem = null;
        boolean[] bestDifference = null;
        boolean[] difference;
        float match;

        // Most likely that a matching item is a neighbour, so count backwards.
        for (int i = size - 1; i >= 0; i--) {
            item1 = items.get(i);
            difference = comparator.difference(item1, item);
            match = comparator.match(item1, item, difference);
            if ((match >= MATCH_GOOD) && (match > bestMatch)) {
                bestMatch = match;
                bestDifference = difference;
                bestItem = item1;
                if (match == 1f) {
                    break;
                }
            }
        }
        if (bestItem != null) {
            publishProgress(size, bestItem, item, bestMatch, bestDifference);
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
