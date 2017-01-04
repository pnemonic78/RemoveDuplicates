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
import android.content.res.ColorStateList;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.github.android.removeduplicates.R;
import com.github.duplicates.DuplicateComparator;
import com.github.duplicates.DuplicateItemPair;
import com.github.duplicates.DuplicateViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.provider.CallLog.Calls.ANSWERED_EXTERNALLY_TYPE;
import static android.provider.CallLog.Calls.BLOCKED_TYPE;
import static android.provider.CallLog.Calls.INCOMING_TYPE;
import static android.provider.CallLog.Calls.MISSED_TYPE;
import static android.provider.CallLog.Calls.OUTGOING_TYPE;
import static android.provider.CallLog.Calls.REJECTED_TYPE;
import static android.provider.CallLog.Calls.VOICEMAIL_TYPE;

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

    private final ColorStateList colorDate;
    private final ColorStateList colorDuration;
    private final ColorStateList colorName;
    private final ColorStateList colorNumber;
    private final ColorStateList colorType;

    private CallLogItem item1;
    private CallLogItem item2;

    public CallLogViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        colorDate = date1.getTextColors();
        colorDuration = duration1.getTextColors();
        colorName = name1.getTextColors();
        colorNumber = number1.getTextColors();
        colorType = type1.getTextColors();
    }

    @Override
    public void bind(DuplicateItemPair<CallLogItem> pair) {
        this.item1 = pair.getItem1();
        this.item2 = pair.getItem2();
        Context context = itemView.getContext();

        match.setText(context.getString(R.string.match, percentFormatter.format(pair.getMatch())));

        checkbox1.setChecked(item1.isChecked());
        date1.setText(DateUtils.formatDateTime(context, item1.getDate(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_ABBREV_ALL));
        duration1.setText(DateUtils.formatElapsedTime(item1.getDuration()));
        type1.setText(getTypeName(context, item1.getType()));
        number1.setText(item1.getNumber());
        name1.setText(item1.getName());

        checkbox2.setChecked(item2.isChecked());
        date2.setText(DateUtils.formatDateTime(context, item2.getDate(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_ABBREV_ALL));
        duration2.setText(DateUtils.formatElapsedTime(item2.getDuration()));
        type2.setText(getTypeName(context, item2.getType()));
        number2.setText(item2.getNumber());
        name2.setText(item2.getName());

        highlightDifferences(item1, item2);
    }

    protected void highlightDifferences(CallLogItem item1, CallLogItem item2) {
        if (DuplicateComparator.compare(item1.getDate(), item2.getDate()) != 0) {
            date1.setTextColor(colorDifferent);
            date2.setTextColor(colorDifferent);
        } else {
            date1.setTextColor(colorDate);
            date2.setTextColor(colorDate);
        }

        if (DuplicateComparator.compare(item1.getDuration(), item2.getDuration()) != 0) {
            duration1.setTextColor(colorDifferent);
            duration2.setTextColor(colorDifferent);
        } else {
            duration1.setTextColor(colorDuration);
            duration2.setTextColor(colorDuration);
        }

        if (DuplicateComparator.compare(item1.getType(), item2.getType()) != 0) {
            type1.setTextColor(colorDifferent);
            type2.setTextColor(colorDifferent);
        } else {
            type1.setTextColor(colorType);
            type2.setTextColor(colorType);
        }

        if (DuplicateComparator.compare(item1.getNumber(), item2.getNumber()) != 0) {
            number1.setTextColor(colorDifferent);
            number2.setTextColor(colorDifferent);
        } else {
            number1.setTextColor(colorNumber);
            number2.setTextColor(colorNumber);
        }

        if (DuplicateComparator.compare(item1.getName(), item2.getName()) != 0) {
            name1.setTextColor(colorDifferent);
            name2.setTextColor(colorDifferent);
        } else {
            name1.setTextColor(colorName);
            name2.setTextColor(colorName);
        }
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
        item1.setChecked(checkbox1.isChecked());
    }

    @OnClick(R.id.checkbox2)
    void checkbox2Clicked() {
        item2.setChecked(checkbox2.isChecked());
    }
}
