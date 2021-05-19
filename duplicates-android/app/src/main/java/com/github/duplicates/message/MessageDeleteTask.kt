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
package com.github.duplicates.message

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import androidx.preference.PreferenceManager
import com.github.duplicates.DuplicateDeleteTask
import com.github.duplicates.DuplicateDeleteTaskListener

/**
 * Task to find duplicate messages.
 *
 * @author moshe.w
 */
class MessageDeleteTask<L : DuplicateDeleteTaskListener<MessageItem>>(
    context: Context,
    listener: L
) : DuplicateDeleteTask<MessageItem, L>(context, listener) {

    override fun createProvider(context: Context): MessageProvider {
        return MessageProvider(context)
    }

    override fun checkPermissions(activity: Activity) {
        if (checkDefaultSmsPackage(activity)) {
            super.checkPermissions(activity)
        } else {
            setDefaultSmsPackage(activity)
        }
    }

    override fun onPostExecute(result: Unit) {
        super.onPostExecute(result)
        restoreDefaultSmsPackage()
    }

    /**
     * Android 4.4 (KitKat) makes the existing APIs public and adds the concept of a default SMS app, which the user can select in system settings.
     *
     * @param context The context.
     */
    protected fun checkDefaultSmsPackage(context: Context): Boolean {
        val defaultSmsApp = Telephony.Sms.getDefaultSmsPackage(context)
        val packageName = context.packageName
        return packageName == defaultSmsApp
    }

    /**
     * Set this app as the default SMS app.
     *
     * @param activity the activity.
     */
    private fun setDefaultSmsPackage(activity: Activity) {
        val defaultSmsApp = Telephony.Sms.getDefaultSmsPackage(activity)
        val packageName = activity.packageName

        val prefs = PreferenceManager.getDefaultSharedPreferences(activity)
        prefs.edit().putString(KEY_SMS_PACKAGE, defaultSmsApp).apply()

        val intent = Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT)
        intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, packageName)
        activity.startActivityForResult(intent, ACTIVITY_SMS_PACKAGE)
    }

    /**
     * Restore the default SMS app.
     */
    private fun restoreDefaultSmsPackage() {
        val context = context
        var defaultSmsApp: String? = Telephony.Sms.getDefaultSmsPackage(context)
        val packageName = context.packageName

        if (packageName == defaultSmsApp) {
            val prefs = PreferenceManager.getDefaultSharedPreferences(context)
            defaultSmsApp = prefs.getString(KEY_SMS_PACKAGE, null)

            if (defaultSmsApp != null) {
                val intent = Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT)
                intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, defaultSmsApp)
                context.startActivity(intent)
            }
        }
    }

    override fun onActivityResult(
        activity: Activity,
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(activity, requestCode, resultCode, data)

        if (requestCode == ACTIVITY_SMS_PACKAGE) {
            if (checkDefaultSmsPackage(activity)) {
                super.checkPermissions(activity)
            }
        }
    }

    override fun onCancelled() {
        super.onCancelled()
        restoreDefaultSmsPackage()
    }

    companion object {

        private const val KEY_SMS_PACKAGE = "defaultSmsPackage"

        const val ACTIVITY_SMS_PACKAGE = 30
    }
}
