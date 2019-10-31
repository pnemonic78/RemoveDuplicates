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
package com.github.duplicates.alarm

import android.content.Context
import android.content.pm.PackageManager
import com.github.duplicates.DefaultProvider
import com.github.duplicates.DuplicateProvider
import com.github.duplicates.WrapperProvider

/**
 * Provide duplicate alarms.
 *
 * @author moshe.w
 */
class AlarmProvider(context: Context) : WrapperProvider<AlarmItem>(context) {

    override fun createDelegate(context: Context): DuplicateProvider<AlarmItem> {
        if (hasSamsungProvider == null) {
            val pm = context.packageManager
            try {
                val packageInfo = pm.getPackageInfo(SamsungAlarmProvider.PACKAGE, PackageManager.GET_PROVIDERS)
                hasSamsungProvider = (packageInfo.providers != null) && packageInfo.providers.isNotEmpty()
            } catch (e: PackageManager.NameNotFoundException) {
                hasSamsungProvider = false
                e.printStackTrace()
            }
        }

        return if (hasSamsungProvider == true) {
            SamsungAlarmProvider(context)
        } else {
            DefaultProvider(context)
        }
    }

    companion object {

        private var hasSamsungProvider: Boolean? = null
    }
}
