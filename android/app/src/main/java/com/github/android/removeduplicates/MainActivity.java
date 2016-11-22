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
package com.github.android.removeduplicates;

import android.app.Activity;
import android.os.Bundle;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Main activity.
 *
 * @author moshe.w
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.item_alarms)
    void removeDuplicateAlarms() {
        //TODO implement me!
    }

    @OnClick(R.id.item_bookmarks)
    void removeDuplicateBookmarks() {
        //TODO implement me!
    }

    @OnClick(R.id.item_calendar)
    void removeDuplicateCalendar() {
        //TODO implement me!
    }

    @OnClick(R.id.item_call_log)
    void removeDuplicateCallLog() {
        //TODO implement me!
    }

    @OnClick(R.id.item_contacts)
    void removeDuplicateContacts() {
        //TODO implement me!
    }

    @OnClick(R.id.item_messages)
    void removeDuplicateMessages() {
        //TODO implement me!
    }
}
