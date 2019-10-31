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

import android.content.Context

import com.github.duplicates.DuplicateFindTask
import com.github.duplicates.DuplicateTask
import com.github.duplicates.DuplicateTaskListener

/**
 * Task to find duplicate calendar events.
 *
 * @author moshe.w
 */
class CalendarFindTask(context: Context, listener: DuplicateTaskListener<CalendarItem, DuplicateTask<CalendarItem, Any, Any, List<CalendarItem>>>) : DuplicateFindTask<CalendarItem, CalendarViewHolder>(context, listener) {

    override fun createProvider(context: Context): CalendarProvider {
        return CalendarProvider(context)
    }

    override fun createAdapter(): CalendarAdapter {
        return CalendarAdapter()
    }

    override fun createComparator(): CalendarComparator {
        return CalendarComparator()
    }
}
