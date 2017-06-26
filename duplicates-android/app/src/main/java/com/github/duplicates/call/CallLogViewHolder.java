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
package com.github.duplicates.call;

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

import static com.github.duplicates.call.CallLogComparator.DATE;
import static com.github.duplicates.call.CallLogComparator.DURATION;
import static com.github.duplicates.call.CallLogComparator.NAME;
import static com.github.duplicates.call.CallLogComparator.NUMBER;
import static com.github.duplicates.call.CallLogComparator.TYPE;
import static com.github.duplicates.call.CallLogItem.ANSWERED_EXTERNALLY_TYPE;
import static com.github.duplicates.call.CallLogItem.BLOCKED_TYPE;
import static com.github.duplicates.call.CallLogItem.INCOMING_TYPE;
import static com.github.duplicates.call.CallLogItem.MISSED_TYPE;
import static com.github.duplicates.call.CallLogItem.OUTGOING_TYPE;
import static com.github.duplicates.call.CallLogItem.REJECTED_TYPE;
import static com.github.duplicates.call.CallLogItem.VOICEMAIL_TYPE;

/**
 * View holder of a duplicate call.
 *
 * @author moshe.w
 */
public class CallLogViewHolder extends DuplicateViewHolder<CallLogItem> {

    @BindView(R.id.match)
    TextView match;

    @BindView(R.id.checkbox1)
    CheckBox checkbox1;
    @BindView(R.id.date1)
    TextView date1;
    @BindView(R.id.duration1)
    TextView duration1;
    @BindView(R.id.number1)
    TextView number1;
    @BindView(R.id.type1)
    TextView type1;
    @BindView(R.id.name1)
    TextView name1;

    @BindView(R.id.checkbox2)
    CheckBox checkbox2;
    @BindView(R.id.date2)
    TextView date2;
    @BindView(R.id.duration2)
    TextView duration2;
    @BindView(R.id.number2)
    TextView number2;
    @BindView(R.id.type2)
    TextView type2;
    @BindView(R.id.name2)
    TextView name2;

    public CallLogViewHolder(View itemView) {
        this(itemView, null);
    }

    public CallLogViewHolder(View itemView, OnItemCheckedChangeListener onCheckedChangeListener) {
        super(itemView, onCheckedChangeListener);
        ButterKnife.bind(this, itemView);
    }

    @Override
    protected void bindHeader(Context context, DuplicateItemPair<CallLogItem> pair) {
        match.setText(context.getString(R.string.match, percentFormatter.format(pair.getMatch())));
    }

    @Override
    protected void bindItem1(Context context, CallLogItem item) {
        checkbox1.setChecked(item.isChecked());
        date1.setText(DateUtils.formatDateTime(context, item.getDate(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_ABBREV_ALL));
        duration1.setText(DateUtils.formatElapsedTime(item.getDuration()));
        type1.setText(getTypeName(context, item.getType()));
        number1.setText(item.getNumber());
        name1.setText(item.getName());
    }

    @Override
    protected void bindItem2(Context context, CallLogItem item) {
        checkbox2.setChecked(item.isChecked());
        date2.setText(DateUtils.formatDateTime(context, item.getDate(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_ABBREV_ALL));
        duration2.setText(DateUtils.formatElapsedTime(item.getDuration()));
        type2.setText(getTypeName(context, item.getType()));
        number2.setText(item.getNumber());
        name2.setText(item.getName());
    }

    @Override
    protected void bindDifference(Context context, DuplicateItemPair<CallLogItem> pair) {
        boolean[] difference = pair.getDifference();

        bindDifference(date1, date2, difference[DATE]);
        bindDifference(duration1, duration2, difference[DURATION]);
        bindDifference(type1, type2, difference[TYPE]);
        bindDifference(number1, number2, difference[NUMBER]);
        bindDifference(name1, name2, difference[NAME]);
    }

    private CharSequence getTypeName(Context context, int type) {
        switch (type) {
            case INCOMING_TYPE:
                return context.getText(R.string.call_type_incoming);
            case OUTGOING_TYPE:
                return context.getText(R.string.call_type_outgoing);
            case MISSED_TYPE:
                return context.getText(R.string.call_type_missed);
            case VOICEMAIL_TYPE:
                return context.getText(R.string.call_type_voicemail);
            case REJECTED_TYPE:
                return context.getText(R.string.call_type_rejected);
            case BLOCKED_TYPE:
                return context.getText(R.string.call_type_blocked);
            case ANSWERED_EXTERNALLY_TYPE:
                return context.getText(R.string.call_type_external);
        }
        return context.getText(R.string.call_type_other);
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
