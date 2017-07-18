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
package com.github.duplicates.bookmark;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.android.removeduplicates.BuildConfig;
import com.github.android.removeduplicates.R;
import com.github.duplicates.DuplicateItemPair;
import com.github.duplicates.DuplicateViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.github.duplicates.bookmark.BookmarkComparator.CREATED;
import static com.github.duplicates.bookmark.BookmarkComparator.DATE;
import static com.github.duplicates.bookmark.BookmarkComparator.FAVICON;
import static com.github.duplicates.bookmark.BookmarkComparator.TITLE;
import static com.github.duplicates.bookmark.BookmarkComparator.URL;

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

    public BookmarkViewHolder(View itemView) {
        this(itemView, null);
    }

    public BookmarkViewHolder(View itemView, OnItemCheckedChangeListener onCheckedChangeListener) {
        super(itemView, onCheckedChangeListener);
        ButterKnife.bind(this, itemView);
    }

    @Override
    protected void bindHeader(Context context, DuplicateItemPair<BookmarkItem> pair) {
        match.setText(context.getString(R.string.match, percentFormatter.format(pair.getMatch())));
    }

    @Override
    protected void bindItem1(Context context, BookmarkItem item) {
        checkbox1.setChecked(item.isChecked());
        checkbox1.setText(BuildConfig.DEBUG ? Long.toString(item.getId()) : "");
        created1.setText(DateUtils.formatDateTime(context, item.getCreated(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_ABBREV_ALL));
        date1.setText(DateUtils.formatDateTime(context, item.getDate(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_ABBREV_ALL));
        icon1.setImageBitmap(item.getIcon());
        title1.setText(item.getTitle());
        url1.setText(item.getUrl() != null ? item.getUrl().toString() : null);
    }

    @Override
    protected void bindItem2(Context context, BookmarkItem item) {
        checkbox2.setChecked(item.isChecked());
        checkbox2.setText(BuildConfig.DEBUG ? Long.toString(item.getId()) : "");
        created2.setText(DateUtils.formatDateTime(context, item.getCreated(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_ABBREV_ALL));
        date2.setText(DateUtils.formatDateTime(context, item.getDate(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_ABBREV_ALL));
        icon2.setImageBitmap(item.getIcon());
        title2.setText(item.getTitle());
        url2.setText(item.getUrl() != null ? item.getUrl().toString() : null);
    }

    @Override
    protected void bindDifference(Context context, DuplicateItemPair<BookmarkItem> pair) {
        boolean[] difference = pair.getDifference();

        bindDifference(created1, created2, difference[CREATED]);
        bindDifference(date1, date2, difference[DATE]);
        bindDifference(icon1, icon2, difference[FAVICON]);
        bindDifference(title1, title2, difference[TITLE]);
        bindDifference(url1, url2, difference[URL]);
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
