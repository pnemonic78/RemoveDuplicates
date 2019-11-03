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

import com.github.android.removeduplicates.BuildConfig
import com.github.android.removeduplicates.R

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

/**
 * Main spinner items.
 *
 * @author moshe.w
 */
enum class MainSpinnerItem(@param:StringRes @field:StringRes val label: Int,
                           @param:DrawableRes @field:DrawableRes val icon: Int,
                           val enabled: Boolean = true) {

    ALARMS(R.string.item_alarms, R.drawable.ic_alarm),
    BOOKMARKS(R.string.item_bookmarks, R.drawable.ic_bookmark),
    CALENDAR(R.string.item_calendar, R.drawable.ic_event),
    CALL_LOG(R.string.item_call_log, R.drawable.ic_call, BuildConfig.DEBUG),
    CONTACTS(R.string.item_contacts, R.drawable.ic_contacts),
    MESSAGES(R.string.item_messages, R.drawable.ic_message, BuildConfig.DEBUG)
}
