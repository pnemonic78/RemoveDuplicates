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

/**
 * Task for duplicates.
 *
 * @author moshe.w
 */
public abstract class DuplicateTask<T extends DuplicateItem, Params, Progress, Result> extends AsyncTask<Params, Progress, Result>
        implements DuplicateProviderListener<T, DuplicateProvider<T>> {

    private final Context context;
    private final DuplicateTaskListener listener;
    private DuplicateProvider<T> provider;

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

    /**
     * Get the system provider. Create the provider if necessary.
     *
     * @return the provider.
     */
    protected DuplicateProvider getProvider() {
        if (provider == null) {
            provider = createProvider(getContext());
        }
        return provider;
    }

    @Override
    protected void onPreExecute() {
        getListener().onDuplicateTaskStarted(this);
        this.provider = getProvider();
        provider.setListener(this);
    }

    @Override
    protected void onProgressUpdate(Progress... progress) {
        if (progress.length == 1) {
            getListener().onDuplicateTaskProgress(this, (Integer) progress[0]);
        }
    }

    @Override
    protected void onPostExecute(Result result) {
        getListener().onDuplicateTaskFinished(this);
    }

    @Override
    protected void onCancelled() {
        getListener().onDuplicateTaskCancelled(this);
    }
}
