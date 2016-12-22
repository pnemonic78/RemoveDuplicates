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
package com.github.duplicates.message;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (checkDefaultSmsPackage(activity)) {
                super.checkPermissions(activity);
            } else {
                setDefaultSmsPackage(activity);
            }
        } else {
            super.checkPermissions(activity);
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
    @TargetApi(Build.VERSION_CODES.KITKAT)
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
    @TargetApi(Build.VERSION_CODES.KITKAT)
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
    @TargetApi(Build.VERSION_CODES.KITKAT)
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
