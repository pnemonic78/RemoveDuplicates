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

import static com.github.duplicates.DuplicateComparator.SAME;
import static com.github.duplicates.DuplicateComparator.compare;
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

    private MessageItem item1;
    private MessageItem item2;

    public MessageViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bind(DuplicateItemPair<MessageItem> pair) {
        this.item1 = pair.getItem1();
        this.item2 = pair.getItem2();
        Context context = itemView.getContext();

        match.setText(context.getString(R.string.match, percentFormatter.format(pair.getMatch())));

        checkbox1.setChecked(item1.isChecked());
        date1.setText(DateUtils.formatDateTime(context, item1.getDateReceived(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_ABBREV_ALL));
        address1.setText(item1.getAddress());
        type1.setText(getTypeName(context, item1.getType()));
        body1.setText(item1.getBody());

        checkbox2.setChecked(item2.isChecked());
        date2.setText(DateUtils.formatDateTime(context, item2.getDateReceived(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_ABBREV_ALL));
        address2.setText(item2.getAddress());
        type2.setText(getTypeName(context, item2.getType()));
        body2.setText(item2.getBody());

        highlightDifferences(item1, item2);
    }

    protected void highlightDifferences(MessageItem item1, MessageItem item2) {
        if (compare(item1.getDateReceived(), item2.getDateReceived()) != SAME) {
            date1.setBackgroundDrawable(colorDifferent);
            date2.setBackgroundDrawable(colorDifferent);
        } else {
            date1.setBackgroundDrawable(null);
            date2.setBackgroundDrawable(null);
        }

        if (compare(item1.getAddress(), item2.getAddress()) != SAME) {
            address1.setBackgroundDrawable(colorDifferent);
            address2.setBackgroundDrawable(colorDifferent);
        } else {
            address1.setBackgroundDrawable(null);
            address2.setBackgroundDrawable(null);
        }

        if (compare(item1.getType(), item2.getType()) != SAME) {
            type1.setBackgroundDrawable(colorDifferent);
            type2.setBackgroundDrawable(colorDifferent);
        } else {
            type1.setBackgroundDrawable(null);
            type2.setBackgroundDrawable(null);
        }

        if (compare(item1.getBody(), item2.getBody()) != SAME) {
            body1.setBackgroundDrawable(colorDifferent);
            body2.setBackgroundDrawable(colorDifferent);
        } else {
            body1.setBackgroundDrawable(null);
            body2.setBackgroundDrawable(null);
        }
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
