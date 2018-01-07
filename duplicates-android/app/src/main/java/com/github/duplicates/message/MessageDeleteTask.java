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
package com.github.duplicates.message;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.Telephony;

import com.github.duplicates.DuplicateDeleteTask;
import com.github.duplicates.DuplicateTaskListener;

/**
 * Task to find duplicate messages.
 *
 * @author moshe.w
 */
public class MessageDeleteTask extends DuplicateDeleteTask<MessageItem> {

    private static final String KEY_SMS_PACKAGE = "defaultSmsPackage";

    public static final int ACTIVITY_SMS_PACKAGE = 30;

    public MessageDeleteTask(Context context, DuplicateTaskListener listener) {
        super(context, listener);
    }

    @Override
    protected MessageProvider createProvider(Context context) {
        return new MessageProvider(context);
    }

    @Override
    protected void checkPermissions(Activity activity) {
        if (checkDefaultSmsPackage(activity)) {
            super.checkPermissions(activity);
        } else {
            setDefaultSmsPackage(activity);
        }
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        restoreDefaultSmsPackage();
    }

    /**
     * Android 4.4 (KitKat) makes the existing APIs public and adds the concept of a default SMS app, which the user can select in system settings.
     *
     * @param context The context.
     */
    protected boolean checkDefaultSmsPackage(Context context) {
        String defaultSmsApp = Telephony.Sms.getDefaultSmsPackage(context);
        String packageName = context.getPackageName();
        return packageName.equals(defaultSmsApp);
    }

    /**
     * Set this app as the default SMS app.
     *
     * @param activity the activity.
     */
    protected void setDefaultSmsPackage(Activity activity) {
        Context context = activity;
        String defaultSmsApp = Telephony.Sms.getDefaultSmsPackage(context);
        String packageName = context.getPackageName();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putString(KEY_SMS_PACKAGE, defaultSmsApp).apply();

        Intent intent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
        intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, packageName);
        activity.startActivityForResult(intent, ACTIVITY_SMS_PACKAGE);
    }

    /**
     * Restore the default SMS app.
     */
    protected void restoreDefaultSmsPackage() {
        Context context = getContext();
        String defaultSmsApp = Telephony.Sms.getDefaultSmsPackage(context);
        String packageName = context.getPackageName();

        if (packageName.equals(defaultSmsApp)) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            defaultSmsApp = prefs.getString(KEY_SMS_PACKAGE, null);

            if (defaultSmsApp != null) {
                Intent intent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
                intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, defaultSmsApp);
                context.startActivity(intent);
            }
        }
    }

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        super.onActivityResult(activity, requestCode, resultCode, data);

        if (requestCode == ACTIVITY_SMS_PACKAGE) {
            if (checkDefaultSmsPackage(activity)) {
                super.checkPermissions(activity);
            }
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        restoreDefaultSmsPackage();
    }
}
