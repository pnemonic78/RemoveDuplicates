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
package com.github.duplicates.alarm;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.github.duplicates.WrapperProvider;
import com.github.duplicates.DuplicateProvider;

/**
 * Provide duplicate alarms.
 *
 * @author moshe.w
 */
public class AlarmProvider extends WrapperProvider<AlarmItem> {

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
