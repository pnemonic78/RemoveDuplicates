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

import java.text.NumberFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * View holder of a duplicate call.
 *
 * @author moshe.w
 */
public class CallLogViewHolder extends DuplicateViewHolder<CallLogItem> {

    /**
     * CallLog type: all messages.
     */
    public static final int MESSAGE_TYPE_ALL = 0;//TextBasedSmsColumns.MESSAGE_TYPE_ALL
    /**
     * CallLog type: inbox.
     */
    public static final int MESSAGE_TYPE_INBOX = 1;//TextBasedSmsColumns.MESSAGE_TYPE_INBOX
    /**
     * CallLog type: sent messages.
     */
    public static final int MESSAGE_TYPE_SENT = 2;//TextBasedSmsColumns.MESSAGE_TYPE_SENT
    /**
     * CallLog type: drafts.
     */
    public static final int MESSAGE_TYPE_DRAFT = 3;//TextBasedSmsColumns.MESSAGE_TYPE_DRAFT
    /**
     * CallLog type: outbox.
     */
    public static final int MESSAGE_TYPE_OUTBOX = 4;//TextBasedSmsColumns.MESSAGE_TYPE_OUTBOX
    /**
     * CallLog type: failed outgoing message.
     */
    public static final int MESSAGE_TYPE_FAILED = 5;//TextBasedSmsColumns.MESSAGE_TYPE_FAILED
    /**
     * CallLog type: queued to send later.
     */
    public static final int MESSAGE_TYPE_QUEUED = 6;//TextBasedSmsColumns.MESSAGE_TYPE_QUEUED

    @BindView(R.id.match)
    TextView match;
    @BindView(R.id.checkbox1)
    CheckBox checkbox1;
    @BindView(R.id.date1)
    TextView date1;
    @BindView(R.id.address1)
    TextView address1;
    @BindView(R.id.type1)
    TextView type1;
    @BindView(R.id.body1)
    TextView body1;
    @BindView(R.id.checkbox2)
    CheckBox checkbox2;
    @BindView(R.id.date2)
    TextView date2;
    @BindView(R.id.address2)
    TextView address2;
    @BindView(R.id.type2)
    TextView type2;
    @BindView(R.id.body2)
    TextView body2;

    private final NumberFormat formatter = NumberFormat.getPercentInstance();
    private final ColorStateList colorDate;
    private final ColorStateList colorAddress;
    private final ColorStateList colorType;
    private final ColorStateList colorBody;

    private CallLogItem item1;
    private CallLogItem item2;

    public CallLogViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        colorDate = date1.getTextColors();
        colorAddress = address1.getTextColors();
        colorType = type1.getTextColors();
        colorBody = body1.getTextColors();
    }

    @Override
    public void bind(DuplicateItemPair<CallLogItem> pair) {
        this.item1 = pair.getItem1();
        this.item2 = pair.getItem2();
        Context context = itemView.getContext();

        match.setText(context.getString(R.string.match, formatter.format(pair.getMatch())));

        checkbox1.setChecked(item1.isChecked());
        date1.setText(DateUtils.formatDateTime(context, item1.getDate(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_ABBREV_ALL));
//        address1.setText(item1.getAddress());
        type1.setText(getTypeName(context, item1.getType()));
//        body1.setText(item1.getBody());

        checkbox2.setChecked(item2.isChecked());
        date2.setText(DateUtils.formatDateTime(context, item2.getDate(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_ABBREV_ALL));
//        address2.setText(item2.getAddress());
        type2.setText(getTypeName(context, item2.getType()));
//        body2.setText(item2.getBody());

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

//        if (DuplicateComparator.compare(item1.getAddress(), item2.getAddress()) != 0) {
//            address1.setTextColor(colorDifferent);
//            address2.setTextColor(colorDifferent);
//        } else {
//            address1.setTextColor(colorAddress);
//            address2.setTextColor(colorAddress);
//        }

        if (DuplicateComparator.compare(item1.getType(), item2.getType()) != 0) {
            type1.setTextColor(colorDifferent);
            type2.setTextColor(colorDifferent);
        } else {
            type1.setTextColor(colorType);
            type2.setTextColor(colorType);
        }

//        if (DuplicateComparator.compare(item1.getBody(), item2.getBody()) != 0) {
//            body1.setTextColor(colorDifferent);
//            body2.setTextColor(colorDifferent);
//        } else {
//            body1.setTextColor(colorBody);
//            body2.setTextColor(colorBody);
//        }
    }

    private CharSequence getTypeName(Context context, int type) {
        switch (type) {
            case MESSAGE_TYPE_ALL:
                return context.getText(R.string.message_type_all);
            case MESSAGE_TYPE_DRAFT:
                return context.getText(R.string.message_type_drafts);
            case MESSAGE_TYPE_FAILED:
                return context.getText(R.string.message_type_failed);
            case MESSAGE_TYPE_INBOX:
                return context.getText(R.string.message_type_inbox);
            case MESSAGE_TYPE_OUTBOX:
                return context.getText(R.string.message_type_outbox);
            case MESSAGE_TYPE_QUEUED:
                return context.getText(R.string.message_type_queued);
            case MESSAGE_TYPE_SENT:
                return context.getText(R.string.message_type_sent);
        }
        return null;
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
