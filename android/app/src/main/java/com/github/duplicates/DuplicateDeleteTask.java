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
public abstract class DuplicateDeleteTask<T extends DuplicateItem> extends DuplicateTask<T, T, Object, Void> {

    private final List<T> items = new ArrayList<>();

    public DuplicateDeleteTask(Context context, DuplicateTaskListener listener) {
        super(context, listener);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        items.clear();
    }

    @Override
    protected Void doInBackground(T... params) {
        if (params != null) {
            for (T item : params) {
                if (item.getId() != 0L) {
                    items.add(item);
                }
            }
        }
        publishProgress(items.size());
        try {
            getProvider().deleteItems(items);
        } catch (CancellationException e) {
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Object... progress) {
        getListener().onDuplicateTaskProgress(this, (Integer) progress[0]);
        if (progress.length > 1) {
            T item = (T) progress[1];
            getListener().onDuplicateTaskItemDeleted(this, item);
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
    protected String[] getPermissions() {
        return getProvider().getDeletePermissions();
    }
}
