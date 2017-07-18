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
package com.github.duplicates.calendar;

import android.content.Context;

import com.github.duplicates.DuplicateFindTask;
import com.github.duplicates.DuplicateTaskListener;

/**
 * Task to find duplicate calendar events.
 *
 * @author moshe.w
 */
public class CalendarFindTask extends DuplicateFindTask<CalendarItem, CalendarViewHolder> {

    public CalendarFindTask(Context context, DuplicateTaskListener listener) {
        super(context, listener);
    }

    @Override
    protected CalendarProvider createProvider(Context context) {
        return new CalendarProvider(context);
    }

    @Override
    public CalendarAdapter createAdapter() {
        return new CalendarAdapter();
    }

    @Override
    public CalendarComparator createComparator() {
        return new CalendarComparator();
    }
}
