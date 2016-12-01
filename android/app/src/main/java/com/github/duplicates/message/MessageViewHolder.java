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
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bind(DuplicateItemPair<MessageItem> pair) {
        MessageItem item1 = pair.getItem1();
        MessageItem item2 = pair.getItem2();
        Context context = itemView.getContext();

        match.setText(context.getString(R.string.match, "80%"));

        checkbox1.setChecked(false);
        date1.setText(DateUtils.formatDateTime(context, item1.getDateReceived(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME));
        address1.setText(item1.getAddress());
        type1.setText(Integer.toString(item1.getType()));
        body1.setText(item1.getBody());

        checkbox2.setChecked(true);
        date2.setText(DateUtils.formatDateTime(context, item2.getDateReceived(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME));
        address2.setText(item2.getAddress());
        type2.setText(Integer.toString(item2.getType()));
        body2.setText(item2.getBody());
    }
}
