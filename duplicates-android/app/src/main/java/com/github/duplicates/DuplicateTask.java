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

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.github.android.removeduplicates.R;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

/**
 * Task for duplicates.
 *
 * @author moshe.w
 */
public abstract class DuplicateTask<T extends DuplicateItem, Params, Progress, Result> extends AsyncTask<Params, Progress, Result>
        implements DuplicateProviderListener<T, DuplicateProvider<T>> {

    /**
     * Activity id for requesting location permissions.
     */
    public static final int ACTIVITY_PERMISSIONS = 2;

    private final Context context;
    private final DuplicateTaskListener listener;
    private DuplicateProvider<T> provider;
    private Params[] params;

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
        provider.onPreExecute();
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
        getProvider().onPostExecute();
    }

    @Override
    protected void onCancelled() {
        getListener().onDuplicateTaskCancelled(this);
    }

    /**
     * Start the task. Checks for permissions and then executes.
     *
     * @param activity the activity for permissions.
     * @param params   the execution parameters.
     */
    public final void start(Activity activity, Params[] params) {
        this.params = params;
        checkPermissions(activity);
    }

    /**
     * Start the task without parameters. Checks for permissions and then executes.
     *
     * @param activity the activity for permissions.
     * @see #start(Activity, Object[])
     */
    public final void start(Activity activity) {
        start(activity, null);
    }

    protected void checkPermissions(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkSelfPermissions(activity);
        } else {
            onPermissionGranted();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    protected void checkSelfPermissions(Activity activity) {
        String[] permissions = getPermissions();
        if (checkSelfPermissions(activity, permissions)) {
            onPermissionGranted();
        } else {
            activity.requestPermissions(permissions, ACTIVITY_PERMISSIONS);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    protected boolean checkSelfPermissions(Context context, @Nullable String[] permissions) {
        if ((permissions != null) && (permissions.length > 0)) {
            for (String permission : permissions) {
                if (context.checkSelfPermission(permission) != PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Callback for the result from requesting permissions.
     *
     * @param requestCode  The request code passed in.
     * @param permissions  The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions which is either {@link PackageManager#PERMISSION_GRANTED} or {@link PackageManager#PERMISSION_DENIED}. Never null.
     */
    @TargetApi(Build.VERSION_CODES.M)
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == ACTIVITY_PERMISSIONS) {
            if (permissions.length > 0) {
                if (grantResults.length > 0) {
                    if (grantResults[0] == PERMISSION_GRANTED) {//FIXME: check entire array.
                        onPermissionGranted();
                    } else {
                        onPermissionDenied();
                    }
                }
            } else {
                onPermissionDenied();
            }
        }
    }

    protected abstract String[] getPermissions();

    /**
     * Permission to execute has been granted.
     */
    protected void onPermissionGranted() {
        execute(params);
        this.params = null;
    }

    /**
     * Permission to execute has been denied.
     */
    protected void onPermissionDenied() {
        Toast.makeText(getContext(), R.string.permissions_denied, Toast.LENGTH_LONG).show();
    }

    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
    }

    /**
     * Cancel the task.
     *
     * @return {@code false} if the task could not be cancelled.
     */
    public boolean cancel() {
        boolean result = cancel(true);
        getProvider().cancel();
        return result;
    }
}
