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

import android.content.Context;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.github.android.removeduplicates.R;
import com.github.duplicates.DuplicateItemPair;
import com.github.duplicates.DuplicateViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.github.duplicates.calendar.CalendarComparator.DESCRIPTION;
import static com.github.duplicates.calendar.CalendarComparator.DTEND;
import static com.github.duplicates.calendar.CalendarComparator.DTSTART;
import static com.github.duplicates.calendar.CalendarComparator.LOCATION;
import static com.github.duplicates.calendar.CalendarComparator.TITLE;

/**
 * View holder of a duplicate calendar events.
 *
 * @author moshe.w
 */
public class CalendarViewHolder extends DuplicateViewHolder<CalendarItem> {

    @BindView(R.id.match)
    TextView match;

    @BindView(R.id.checkbox1)
    CheckBox checkbox1;
    @BindView(R.id.cal_color1)
    View calColor1;
    @BindView(R.id.color1)
    View color1;
    @BindView(R.id.start1)
    TextView start1;
    @BindView(R.id.end1)
    TextView end1;
    @BindView(R.id.title1)
    TextView title1;
    @BindView(R.id.description1)
    TextView description1;
    @BindView(R.id.location1)
    TextView location1;

    @BindView(R.id.checkbox2)
    CheckBox checkbox2;
    @BindView(R.id.cal_color2)
    View calColor2;
    @BindView(R.id.color2)
    View color2;
    @BindView(R.id.start2)
    TextView start2;
    @BindView(R.id.end2)
    TextView end2;
    @BindView(R.id.title2)
    TextView title2;
    @BindView(R.id.description2)
    TextView description2;
    @BindView(R.id.location2)
    TextView location2;

    public CalendarViewHolder(View itemView) {
        this(itemView, null);
    }

    public CalendarViewHolder(View itemView, OnItemCheckedChangeListener onCheckedChangeListener) {
        super(itemView, onCheckedChangeListener);
        ButterKnife.bind(this, itemView);
    }

    @Override
    protected void bindHeader(Context context, DuplicateItemPair<CalendarItem> pair) {
        match.setText(context.getString(R.string.match, percentFormatter.format(pair.getMatch())));
    }

    @Override
    protected void bindItem1(Context context, CalendarItem item) {
        checkbox1.setChecked(item.isChecked());
        start1.setText(DateUtils.formatDateTime(context, item.getStart(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_ABBREV_ALL));
        end1.setText(DateUtils.formatDateTime(context, item.getEnd(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_ABBREV_ALL));
        title1.setText(item.getTitle());
        description1.setText(item.getDescription());
        location1.setText(item.getLocation());
        calColor1.setBackgroundColor(item.getCalendar().getColor());
        color1.setBackgroundColor(item.getColor());
    }

    @Override
    protected void bindItem2(Context context, CalendarItem item) {
        checkbox2.setChecked(item.isChecked());
        start2.setText(DateUtils.formatDateTime(context, item.getStart(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_ABBREV_ALL));
        end2.setText(DateUtils.formatDateTime(context, item.getEnd(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_ABBREV_ALL));
        title2.setText(item.getTitle());
        description2.setText(item.getDescription());
        location2.setText(item.getLocation());
        calColor2.setBackgroundColor(item.getCalendar().getColor());
        color2.setBackgroundColor(item.getColor());
    }

    @Override
    protected void bindDifference(Context context, DuplicateItemPair<CalendarItem> pair) {
        boolean[] difference = pair.getDifference();

        bindDifference(context, description1, description2, difference[DESCRIPTION]);
        bindDifference(context, start1, start2, difference[DTSTART]);
        bindDifference(context, end1, end2, difference[DTEND]);
        bindDifference(context, location1, location2, difference[LOCATION]);
        bindDifference(context, title1, title2, difference[TITLE]);
    }

    @OnClick(R.id.checkbox1)
    void checkbox1Clicked() {
        if (onCheckedChangeListener != null) {
            onCheckedChangeListener.onItemCheckedChangeListener(item1, checkbox1.isChecked());
        }
    }

    @OnClick(R.id.checkbox2)
    void checkbox2Clicked() {
        if (onCheckedChangeListener != null) {
            onCheckedChangeListener.onItemCheckedChangeListener(item2, checkbox2.isChecked());
        }
    }
}
