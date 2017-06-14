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
import android.widget.ImageView;
import android.widget.TextView;

import com.github.android.removeduplicates.R;
import com.github.duplicates.DuplicateItemPair;
import com.github.duplicates.DuplicateViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.github.duplicates.calendar.CalendarComparator.CREATED;
import static com.github.duplicates.calendar.CalendarComparator.DATE;
import static com.github.duplicates.calendar.CalendarComparator.FAVICON;
import static com.github.duplicates.calendar.CalendarComparator.TITLE;
import static com.github.duplicates.calendar.CalendarComparator.URL;

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
    @BindView(R.id.created1)
    TextView created1;
    @BindView(R.id.date1)
    TextView date1;
    @BindView(R.id.icon1)
    ImageView icon1;
    @BindView(R.id.title1)
    TextView title1;
    @BindView(R.id.url1)
    TextView url1;

    @BindView(R.id.checkbox2)
    CheckBox checkbox2;
    @BindView(R.id.created2)
    TextView created2;
    @BindView(R.id.date2)
    TextView date2;
    @BindView(R.id.icon2)
    ImageView icon2;
    @BindView(R.id.title2)
    TextView title2;
    @BindView(R.id.url2)
    TextView url2;

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
        created1.setText(DateUtils.formatDateTime(context, item.getCreated(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_ABBREV_ALL));
        date1.setText(DateUtils.formatDateTime(context, item.getDate(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_ABBREV_ALL));
        icon1.setImageBitmap(item.getIcon());
        title1.setText(item.getTitle());
        url1.setText(item.getUrl() != null ? item.getUrl().toString() : null);
    }

    @Override
    protected void bindItem2(Context context, CalendarItem item) {
        checkbox2.setChecked(item.isChecked());
        created2.setText(DateUtils.formatDateTime(context, item.getCreated(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_ABBREV_ALL));
        date2.setText(DateUtils.formatDateTime(context, item.getDate(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_ABBREV_ALL));
        icon2.setImageBitmap(item.getIcon());
        title2.setText(item.getTitle());
        url2.setText(item.getUrl() != null ? item.getUrl().toString() : null);
    }

    @Override
    protected void bindDifference(Context context, DuplicateItemPair<CalendarItem> pair) {
        boolean[] difference = pair.getDifference();

        bindDifference(context, created1, created2, difference[CREATED]);
        bindDifference(context, date1, date2, difference[DATE]);
        bindDifference(context, icon1, icon2, difference[FAVICON]);
        bindDifference(context, title1, title2, difference[TITLE]);
        bindDifference(context, url1, url2, difference[URL]);
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
