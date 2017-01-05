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

//    ALARMS(R.string.item_alarms, R.drawable.ic_alarm_black),
    BOOKMARKS(R.string.item_bookmarks, R.drawable.ic_bookmark_black),
//    CALENDAR(R.string.item_calendar, R.drawable.ic_event_black),
    CALL_LOG(R.string.item_call_log, R.drawable.ic_call_black),
//    CONTACTS(R.string.item_contacts, R.drawable.ic_contacts_black),
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
