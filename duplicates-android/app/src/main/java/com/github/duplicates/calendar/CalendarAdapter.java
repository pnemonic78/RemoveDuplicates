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
package com.github.duplicates.calendar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.android.removeduplicates.R;
import com.github.duplicates.DuplicateAdapter;

/**
 * List adapter for duplicate calendar events.
 *
 * @author moshe.w
 */
public class CalendarAdapter extends DuplicateAdapter<CalendarItem, CalendarViewHolder> {

    @Override
    public CalendarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.same_calendar, parent, false);
        return new CalendarViewHolder(itemView, this);
    }
}
