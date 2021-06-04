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

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.github.android.removeduplicates.BuildConfig
import com.github.android.removeduplicates.R

/**
 * Main spinner items.
 *
 * @author moshe.w
 */
enum class MainSpinnerItem(
    val type: DuplicateItemType,
    @StringRes val label: Int,
    @DrawableRes val icon: Int,
    val enabled: Boolean = true
) {

    ALARMS(DuplicateItemType.ALARM, R.string.item_alarms, R.drawable.ic_alarm),
    BOOKMARKS(DuplicateItemType.BOOKMARK, R.string.item_bookmarks, R.drawable.ic_bookmark),
    CALENDARS(DuplicateItemType.CALENDAR, R.string.item_calendar, R.drawable.ic_event),
    CALL_LOGS(
        DuplicateItemType.CALL_LOG,
        R.string.item_call_log,
        R.drawable.ic_call,
        BuildConfig.FEATURE_CALL_LOGS
    ),
    CONTACTS(DuplicateItemType.CONTACT, R.string.item_contacts, R.drawable.ic_contacts),
    MESSAGES(
        DuplicateItemType.MESSAGE,
        R.string.item_messages,
        R.drawable.ic_message,
        BuildConfig.FEATURE_SMS
    )
}
