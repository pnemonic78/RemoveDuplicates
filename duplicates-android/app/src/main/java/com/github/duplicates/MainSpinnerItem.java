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
package com.github.duplicates;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import com.github.android.removeduplicates.R;

/**
 * Main spinner items.
 *
 * @author moshe.w
 */
public enum MainSpinnerItem {

    ALARMS(R.string.item_alarms, R.drawable.ic_alarm_black),
    BOOKMARKS(R.string.item_bookmarks, R.drawable.ic_bookmark_black),
    CALENDAR(R.string.item_calendar, R.drawable.ic_event_black),
    CALL_LOG(R.string.item_call_log, R.drawable.ic_call_black),
    CONTACTS(R.string.item_contacts, R.drawable.ic_contacts_black),
    MESSAGES(R.string.item_messages, R.drawable.ic_chat_black);

    @StringRes
    final int label;
    @DrawableRes
    final int icon;

    MainSpinnerItem(@StringRes int label, @DrawableRes int icon) {
        this.label = label;
        this.icon = icon;
    }

    @DrawableRes
    public int getIcon() {
        return icon;
    }

    @StringRes
    public int getLabel() {
        return label;
    }
}
