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
package com.github.duplicates

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.AsyncTask
import android.os.Build
import android.widget.Toast

import com.github.android.removeduplicates.R

import android.content.pm.PackageManager.PERMISSION_GRANTED

/**
 * Task for duplicates.
 *
 * @author moshe.w
 */
abstract class DuplicateTask<I : DuplicateItem, Params, Progress, Result, L : DuplicateTaskListener<I>>(
    @field:SuppressLint("StaticFieldLeak") protected val context: Context,
    protected val listener: L
) : AsyncTask<Params, Progress, Result>(),
    DuplicateProviderListener<I, DuplicateProvider<I>> {

    private var _provider: DuplicateProvider<I>? = null
    private var params: Array<Params>? = null

    protected abstract fun createProvider(context: Context): DuplicateProvider<I>

    /**
     * Get the system provider. Create the provider if necessary.
     *
     * @return the provider.
     */
    val provider: DuplicateProvider<I>
        get() {
            if (_provider == null) {
                _provider = createProvider(context)
            }
            return _provider!!
        }

    override fun onPreExecute() {
        listener.onDuplicateTaskStarted(this)
        val provider = this.provider
        provider.listener = this
        provider.onPreExecute()
    }

    override fun onProgressUpdate(vararg progress: Progress) {
        if (progress.size == 1) {
            listener.onDuplicateTaskProgress(this, progress[0] as Int)
        }
    }

    override fun onPostExecute(result: Result) {
        listener.onDuplicateTaskFinished(this)
        provider.onPostExecute()
    }

    override fun onCancelled() {
        listener.onDuplicateTaskCancelled(this)
    }

    /**
     * Start the task. Checks for permissions and then executes.
     *
     * @param activity the activity for permissions.
     * @param params   the execution parameters.
     */
    fun start(activity: Activity, params: Array<Params>? = null) {
        this.params = params
        checkPermissions(activity)
    }

    protected open fun checkPermissions(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkSelfPermissions(activity)
        } else {
            onPermissionGranted()
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    protected fun checkSelfPermissions(activity: Activity) {
        val permissions = getPermissions()
        if (checkSelfPermissions(activity, permissions)) {
            onPermissionGranted()
        } else if (permissions != null) {
            activity.requestPermissions(permissions, REQUEST_PERMISSIONS)
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    protected fun checkSelfPermissions(context: Context, permissions: Array<String>?): Boolean {
        if (permissions != null && permissions.isNotEmpty()) {
            for (permission in permissions) {
                if (context.checkSelfPermission(permission) != PERMISSION_GRANTED) {
                    return false
                }
            }
        }
        return true
    }

    /**
     * Callback for the result from requesting permissions.
     *
     * @param requestCode  The request code passed in.
     * @param permissions  The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions which is either [PackageManager.PERMISSION_GRANTED] or [PackageManager.PERMISSION_DENIED]. Never null.
     */
    @TargetApi(Build.VERSION_CODES.M)
    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == REQUEST_PERMISSIONS) {
            if (permissions.isNotEmpty() && grantResults.isNotEmpty()) {
                if (grantResults[0] == PERMISSION_GRANTED) {//FIXME: check entire array.
                    onPermissionGranted()
                } else {
                    onPermissionDenied()
                }
            } else {
                onPermissionDenied()
            }
        }
    }

    protected abstract fun getPermissions(): Array<String>?

    /**
     * Permission to execute has been granted.
     */
    protected fun onPermissionGranted() {
        val params = this.params
        if (params == null) {
            execute()
        } else {
            execute(*params)
            this.params = null
        }
    }

    /**
     * Permission to execute has been denied.
     */
    protected fun onPermissionDenied() {
        Toast.makeText(context, R.string.permissions_denied, Toast.LENGTH_LONG).show()
        onCancelled()
    }

    open fun onActivityResult(activity: Activity, requestCode: Int, resultCode: Int, data: Intent?) {}

    /**
     * Cancel the task.
     *
     * @return `false` if the task could not be cancelled.
     */
    fun cancel(): Boolean {
        val result = cancel(true)
        provider.cancel()
        return result
    }

    companion object {

        /**
         * Activity id for requesting location permissions.
         */
        const val REQUEST_PERMISSIONS = 0xA110
    }
}
