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
package com.github.duplicates.bookmark;

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

import static com.github.duplicates.DuplicateComparator.SAME;
import static com.github.duplicates.DuplicateComparator.compare;

/**
 * View holder of a duplicate bookmark.
 *
 * @author moshe.w
 */
public class BookmarkViewHolder extends DuplicateViewHolder<BookmarkItem> {

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

    private BookmarkItem item1;
    private BookmarkItem item2;

    public BookmarkViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bind(DuplicateItemPair<BookmarkItem> pair) {
        this.item1 = pair.getItem1();
        this.item2 = pair.getItem2();
        Context context = itemView.getContext();

        match.setText(context.getString(R.string.match, percentFormatter.format(pair.getMatch())));

        checkbox1.setChecked(item1.isChecked());
        created1.setText(DateUtils.formatDateTime(context, item1.getCreated(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_ABBREV_ALL));
        date1.setText(DateUtils.formatDateTime(context, item1.getDate(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_ABBREV_ALL));
        icon1.setImageBitmap(item1.getIcon());
        title1.setText(item1.getTitle());
        url1.setText(item1.getUrl() != null ? item1.getUrl().toString() : null);

        checkbox2.setChecked(item2.isChecked());
        created2.setText(DateUtils.formatDateTime(context, item2.getCreated(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_ABBREV_ALL));
        date2.setText(DateUtils.formatDateTime(context, item2.getDate(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_ABBREV_ALL));
        icon2.setImageBitmap(item2.getIcon());
        title2.setText(item2.getTitle());
        url2.setText(item2.getUrl() != null ? item2.getUrl().toString() : null);

        highlightDifferences(item1, item2);
    }

    protected void highlightDifferences(BookmarkItem item1, BookmarkItem item2) {
        if (compare(item1.getCreated(), item2.getCreated()) != SAME) {
            created1.setBackgroundDrawable(colorDifferent);
            created2.setBackgroundDrawable(colorDifferent);
        } else {
            created1.setBackgroundDrawable(null);
            created2.setBackgroundDrawable(null);
        }
        if (compare(item1.getDate(), item2.getDate()) != SAME) {
            date1.setBackgroundDrawable(colorDifferent);
            date2.setBackgroundDrawable(colorDifferent);
        } else {
            date1.setBackgroundDrawable(null);
            date2.setBackgroundDrawable(null);
        }
        if (compare(item1.getFavIcon(), item2.getFavIcon()) != SAME) {
            icon1.setBackgroundDrawable(colorDifferent);
            icon2.setBackgroundDrawable(colorDifferent);
        } else {
            icon1.setBackgroundDrawable(null);
            icon2.setBackgroundDrawable(null);
        }
        if (compare(item1.getTitle(), item2.getTitle()) != SAME) {
            title1.setBackgroundDrawable(colorDifferent);
            title2.setBackgroundDrawable(colorDifferent);
        } else {
            title1.setBackgroundDrawable(null);
            title2.setBackgroundDrawable(null);
        }
        if (compare(item1.getUrl(), item2.getUrl()) != SAME) {
            url1.setBackgroundDrawable(colorDifferent);
            url2.setBackgroundDrawable(colorDifferent);
        } else {
            url1.setBackgroundDrawable(null);
            url2.setBackgroundDrawable(null);
        }
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
