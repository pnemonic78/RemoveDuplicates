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
package com.github.duplicates.calendar

import android.graphics.Color
import android.provider.CalendarContract
import android.text.TextUtils
import java.util.*

/**
 * Calendar entity.
 *
 * @author moshe.w
 */
class CalendarEntity {

    var id: Long = 0
    var name: String? = null
    var color = Color.TRANSPARENT
    var timeZone: TimeZone? = null
    var access = CalendarContract.CalendarEntity.CAL_ACCESS_NONE
    var account: String? = null
    var isVisible = true

    val displayName: String?
        get() = if (TextUtils.isEmpty(name)) account else name
}
