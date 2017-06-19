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
package com.github.duplicates.contact;

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

/**
 * View holder of a duplicate contact.
 *
 * @author moshe.w
 */
public class ContactViewHolder extends DuplicateViewHolder<ContactItem> {

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

    public ContactViewHolder(View itemView) {
        this(itemView, null);
    }

    public ContactViewHolder(View itemView, OnItemCheckedChangeListener onCheckedChangeListener) {
        super(itemView, onCheckedChangeListener);
        ButterKnife.bind(this, itemView);
    }

    @Override
    protected void bindHeader(Context context, DuplicateItemPair<ContactItem> pair) {
        match.setText(context.getString(R.string.match, percentFormatter.format(pair.getMatch())));
    }

    @Override
    protected void bindItem1(Context context, ContactItem item) {
        checkbox1.setChecked(item.isChecked());
        date1.setText(DateUtils.formatDateTime(context, item.getDateReceived(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_ABBREV_ALL));
        address1.setText(item.getAddress());
        body1.setText(item.getBody());
    }

    @Override
    protected void bindItem2(Context context, ContactItem item) {
        checkbox2.setChecked(item.isChecked());
        date2.setText(DateUtils.formatDateTime(context, item.getDateReceived(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_ABBREV_ALL));
        address2.setText(item.getAddress());
        body2.setText(item.getBody());
    }

    @Override
    protected void bindDifference(Context context, DuplicateItemPair<ContactItem> pair) {
        boolean[] difference = pair.getDifference();

        bindDifference(context, date1, date2, difference[DATE]);
        bindDifference(context, address1, address2, difference[ADDRESS]);
        bindDifference(context, type1, type2, difference[TYPE]);
        bindDifference(context, body1, body2, difference[BODY]);
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
