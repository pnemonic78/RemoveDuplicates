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
 * Task to delete duplicates.
 *
 * @author moshe.w
 */
public abstract class DuplicateDeleteTask<T extends DuplicateItem> extends DuplicateTask<T, DuplicateItemPair<T>, Object, Void> {

    private final List<DuplicateItemPair<T>> pairs = new ArrayList<>();

    public DuplicateDeleteTask(Context context, DuplicateTaskListener listener) {
        super(context, listener);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pairs.clear();
    }

    @Override
    protected Void doInBackground(DuplicateItemPair<T>... params) {
        if (params != null) {
            for (DuplicateItemPair<T> pair : params) {
                pairs.add(pair);
            }
        }
        publishProgress(pairs.size());
        try {
            getProvider().deletePairs(pairs);
        } catch (CancellationException ignore) {
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Object... progress) {
        getListener().onDuplicateTaskProgress(this, (Integer) progress[0]);
        if (progress.length > 1) {
            Object arg1 = progress[1];
            if (arg1 instanceof DuplicateItem) {
                T item = (T) arg1;
                getListener().onDuplicateTaskItemDeleted(this, item);
            } else {
                DuplicateItemPair<T> pair = (DuplicateItemPair<T>) arg1;
                getListener().onDuplicateTaskPairDeleted(this, pair);
            }
        }
    }

    @Override
    public void onItemFetched(DuplicateProvider<T> provider, int count, T item) {
        // Nothing to do.
    }

    @Override
    public void onItemDeleted(DuplicateProvider<T> provider, int count, T item) {
        publishProgress(count, item);
    }

    @Override
    public void onPairDeleted(DuplicateProvider<T> provider, int count, DuplicateItemPair<T> pair) {
        publishProgress(count, pair);
    }

    @Override
    protected String[] getPermissions() {
        return getProvider().getDeletePermissions();
    }
}
