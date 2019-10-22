/*
 * Copyright 2016, Moshe Waisberg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.duplicates.contact;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.android.removeduplicates.BuildConfig;
import com.github.android.removeduplicates.R;
import com.github.duplicates.DuplicateItemPair;
import com.github.duplicates.DuplicateViewHolder;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.github.duplicates.contact.ContactComparator.EMAIL;
import static com.github.duplicates.contact.ContactComparator.EVENT;
import static com.github.duplicates.contact.ContactComparator.IM;
import static com.github.duplicates.contact.ContactComparator.NAME;
import static com.github.duplicates.contact.ContactComparator.PHONE;

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
    @BindView(R.id.account1)
    TextView account1;
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
    @BindView(R.id.account2)
    TextView account2;
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
        checkbox1.setText(BuildConfig.DEBUG ? Long.toString(item.getId()) : "");
        if (item.getPhotoThumbnailUri() == null) {
            icon1.setImageResource(R.drawable.ic_contact_picture);
        } else {
            icon1.setImageURI(item.getPhotoThumbnailUri());
        }
        account1.setText(context.getString(R.string.contacts_account, item.getAccountName(), item.getAccountType()));
        name1.setText(item.getDisplayName());
        email1.setText(formatData(item.getEmails()));
        event1.setText(formatData(item.getEvents()));
        im1.setText(formatData(item.getIms()));
        phone1.setText(formatData(item.getPhones()));
    }

    @Override
    protected void bindItem2(Context context, ContactItem item) {
        checkbox2.setChecked(item.isChecked());
        checkbox2.setText(BuildConfig.DEBUG ? Long.toString(item.getId()) : "");
        if (item.getPhotoThumbnailUri() == null) {
            icon2.setImageResource(R.drawable.ic_contact_picture);
        } else {
            icon2.setImageURI(item.getPhotoThumbnailUri());
        }
        account2.setText(context.getString(R.string.contacts_account, item.getAccountName(), item.getAccountType()));
        name2.setText(item.getDisplayName());
        email2.setText(formatData(item.getEmails()));
        event2.setText(formatData(item.getEvents()));
        im2.setText(formatData(item.getIms()));
        phone2.setText(formatData(item.getPhones()));
    }

    @Override
    protected void bindDifference(Context context, DuplicateItemPair<ContactItem> pair) {
        boolean[] difference = pair.getDifference();

        bindDifference(email1, email2, difference[EMAIL]);
        bindDifference(event1, event2, difference[EVENT]);
        bindDifference(im1, im2, difference[IM]);
        bindDifference(name1, name2, difference[NAME]);
        bindDifference(phone1, phone2, difference[PHONE]);
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

    protected CharSequence formatData(Collection<? extends ContactData> data) {
        if (data.isEmpty()) {
            return null;
        }
        Set<String> unique = new HashSet<>(data.size());
        for (ContactData datum : data) {
            unique.add(datum.toString());
        }
        StringBuilder s = new StringBuilder();
        for (String u : unique) {
            if (s.length() > 0) {
                s.append("; ");
            }
            s.append(u);
        }
        return s;
    }
}