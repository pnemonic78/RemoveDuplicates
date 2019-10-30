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

import android.text.TextUtils

import com.github.duplicates.DaysOfWeek
import com.github.duplicates.DuplicateItem

/**
 * Duplicate alarm item.
 *
 * @author moshe.w
 */
class AlarmItem : DuplicateItem() {

    var activate: Int = 0
    var alarmTime: Int = 0
    var alertTime: Long = 0
    var createTime: Long = 0
    var isDailyBriefing: Boolean = false
    var name: String? = null
    var notificationType: Int = 0
    var repeat: DaysOfWeek? = null
    var isSnoozeActivate: Boolean = false
    var snoozeDoneCount: Int = 0
    var snoozeDuration: Int = 0
    var snoozeRepeat: Int = 0
    var soundTone: Int = 0
    var soundType: Int = 0
    var soundUri: String? = null
    var isSubdueActivate: Boolean = false
    var subdueDuration: Int = 0
    var subdueTone: Int = 0
    var subdueUri: Int = 0
    var volume: Int = 0

    override fun contains(s: CharSequence): Boolean {
        return name?.contains(s) ?: false
    }
}
