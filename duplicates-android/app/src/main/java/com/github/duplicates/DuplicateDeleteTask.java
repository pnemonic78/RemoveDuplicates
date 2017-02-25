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
        } catch (CancellationException e) {
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
