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

    public DuplicateTask(Context context) {
        this.context = context;
    }

    protected Context getContext() {
        return context;
    }

    @Override
    protected void onPreExecute() {
        //TODO clear the recycler list.
    }

    @Override
    protected List<T> doInBackground(Object... params) {
        DuplicateProvider<T> provider = createProvider(getContext());
        List<T> items = provider.getItems();
        //TODO find duplicates and add to the recycler list.
        return items;
    }

    protected void onProgressUpdate(Integer... progress) {
        //TODO setProgressPercent(progress[0]);
    }

    protected abstract DuplicateProvider<T> createProvider(Context context);
}
