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
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.android.removeduplicates.R;
import com.github.duplicates.DuplicateItemPair;
import com.github.duplicates.DuplicateViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    @BindView(R.id.icon1)
    ImageView icon1;
    @BindView(R.id.name1)
    TextView name1;
    @BindView(R.id.email1)
    TextView email1;
    @BindView(R.id.event1)
    TextView event1;
    @BindView(R.id.im1)
    TextView im1;
    @BindView(R.id.phone1)
    TextView phone1;

    @BindView(R.id.checkbox2)
    CheckBox checkbox2;
    @BindView(R.id.icon2)
    ImageView icon2;
    @BindView(R.id.name2)
    TextView name2;
    @BindView(R.id.email2)
    TextView email2;
    @BindView(R.id.event2)
    TextView event2;
    @BindView(R.id.im2)
    TextView im2;
    @BindView(R.id.phone2)
    TextView phone2;

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
        if (item.getPhotoThumbnailUri() == null) {
            icon1.setImageResource(R.drawable.ic_contact_blank);
        } else {
            icon1.setImageURI(item.getPhotoThumbnailUri());
        }
        name1.setText(item.getDisplayName());
        email1.setText(formatData(item.getEmails()));
        event1.setText(formatData(item.getEvents()));
        im1.setText(formatData(item.getIms()));
        phone1.setText(formatData(item.getPhones()));
    }

    @Override
    protected void bindItem2(Context context, ContactItem item) {
        checkbox2.setChecked(item.isChecked());
        if (item.getPhotoThumbnailUri() == null) {
            icon2.setImageResource(R.drawable.ic_contact_blank);
        } else {
            icon2.setImageURI(item.getPhotoThumbnailUri());
        }
        name2.setText(item.getDisplayName());
        name2.setText(item.getDisplayName());
        email2.setText(formatData(item.getEmails()));
        event2.setText(formatData(item.getEvents()));
        im2.setText(formatData(item.getIms()));
        phone2.setText(formatData(item.getPhones()));
    }

    @Override
    protected void bindDifference(Context context, DuplicateItemPair<ContactItem> pair) {
        boolean[] difference = pair.getDifference();

//        bindDifference(context, date1, date2, difference[DATE]);
//        bindDifference(context, address1, address2, difference[ADDRESS]);
//        bindDifference(context, type1, type2, difference[TYPE]);
//        bindDifference(context, body1, body2, difference[BODY]);
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

    protected CharSequence formatData(List<? extends ContactData> data) {
        final int size = data.size();
        if (size == 0) {
            return null;
        }
        StringBuilder s = new StringBuilder();
        ContactData datum = data.get(0);
        s.append(datum.toString());
        for (int i = 1; i < size; i++) {
            s.append("; ");
            s.append(datum.toString());
        }
        return s;
    }
}
