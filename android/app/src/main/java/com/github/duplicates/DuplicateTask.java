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
public abstract class DuplicateTask<T extends DuplicateItem> extends AsyncTask<Object, Integer, List<T>> {

    private final Context context;
    private final DuplicateTaskListener listener;

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

    @Override
    protected void onPreExecute() {
        listener.onDuplicateTaskStarted(this);
    }

    @Override
    protected List<T> doInBackground(Object... params) {
        DuplicateProvider<T> provider = createProvider(getContext());
        List<T> items = provider.getItems();
        //TODO find duplicates and add to the recycler list.
        for (int i = 0; i < 100; i++) {
            try {
                publishProgress(i);
                Thread.sleep(50L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return items;
    }

    protected void onProgressUpdate(Integer... progress) {
        listener.onDuplicateTaskProgress(this, progress[0]);
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
