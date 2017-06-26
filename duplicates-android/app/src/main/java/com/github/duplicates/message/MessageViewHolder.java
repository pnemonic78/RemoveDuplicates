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
package com.github.duplicates.message;

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

import static com.github.duplicates.message.MessageComparator.ADDRESS;
import static com.github.duplicates.message.MessageComparator.BODY;
import static com.github.duplicates.message.MessageComparator.DATE;
import static com.github.duplicates.message.MessageComparator.TYPE;
import static com.github.duplicates.message.MessageItem.MESSAGE_TYPE_ALL;
import static com.github.duplicates.message.MessageItem.MESSAGE_TYPE_DRAFT;
import static com.github.duplicates.message.MessageItem.MESSAGE_TYPE_FAILED;
import static com.github.duplicates.message.MessageItem.MESSAGE_TYPE_INBOX;
import static com.github.duplicates.message.MessageItem.MESSAGE_TYPE_OUTBOX;
import static com.github.duplicates.message.MessageItem.MESSAGE_TYPE_QUEUED;
import static com.github.duplicates.message.MessageItem.MESSAGE_TYPE_SENT;

/**
 * View holder of a duplicate message.
 *
 * @author moshe.w
 */
public class MessageViewHolder extends DuplicateViewHolder<MessageItem> {

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

    public MessageViewHolder(View itemView) {
        this(itemView, null);
    }

    public MessageViewHolder(View itemView, OnItemCheckedChangeListener onCheckedChangeListener) {
        super(itemView, onCheckedChangeListener);
        ButterKnife.bind(this, itemView);
    }

    @Override
    protected void bindHeader(Context context, DuplicateItemPair<MessageItem> pair) {
        match.setText(context.getString(R.string.match, percentFormatter.format(pair.getMatch())));
    }

    @Override
    protected void bindItem1(Context context, MessageItem item) {
        checkbox1.setChecked(item.isChecked());
        date1.setText(DateUtils.formatDateTime(context, item.getDateReceived(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_ABBREV_ALL));
        address1.setText(item.getAddress());
        type1.setText(getTypeName(context, item.getType()));
        body1.setText(item.getBody());
    }

    @Override
    protected void bindItem2(Context context, MessageItem item) {
        checkbox2.setChecked(item.isChecked());
        date2.setText(DateUtils.formatDateTime(context, item.getDateReceived(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_ABBREV_ALL));
        address2.setText(item.getAddress());
        type2.setText(getTypeName(context, item.getType()));
        body2.setText(item.getBody());
    }

    @Override
    protected void bindDifference(Context context, DuplicateItemPair<MessageItem> pair) {
        boolean[] difference = pair.getDifference();

        bindDifference(date1, date2, difference[DATE]);
        bindDifference(address1, address2, difference[ADDRESS]);
        bindDifference(type1, type2, difference[TYPE]);
        bindDifference(body1, body2, difference[BODY]);
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
