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
package com.github.duplicates.alarm;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.github.duplicates.DelegateProvider;
import com.github.duplicates.DuplicateProvider;

/**
 * Provide duplicate alarms.
 *
 * @author moshe.w
 */
public class AlarmProvider extends DelegateProvider<AlarmItem> {

    private static Boolean hasSamsungProvider;

    public AlarmProvider(Context context) {
        super(context);
    }

    @Override
    protected DuplicateProvider<AlarmItem> createDelegate(Context context) {
        if (hasSamsungProvider == null) {
            PackageManager pm = context.getPackageManager();
            try {
                PackageInfo packageInfo = pm.getPackageInfo(SamsungAlarmProvider.PACKAGE, PackageManager.GET_PROVIDERS);
                hasSamsungProvider = (packageInfo.providers != null) && (packageInfo.providers.length > 0);
            } catch (PackageManager.NameNotFoundException e) {
                hasSamsungProvider = Boolean.FALSE;
                e.printStackTrace();
            }
        }

        if (hasSamsungProvider) {
            return new SamsungAlarmProvider(context);
        }
        return null;
    }
}
