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
package com.github.duplicates.bookmark

import android.content.Context
import android.text.format.DateUtils
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.github.android.removeduplicates.BuildConfig
import com.github.android.removeduplicates.R
import com.github.duplicates.DuplicateItemPair
import com.github.duplicates.DuplicateViewHolder
import com.github.duplicates.bookmark.BookmarkComparator.Companion.CREATED
import com.github.duplicates.bookmark.BookmarkComparator.Companion.DATE
import com.github.duplicates.bookmark.BookmarkComparator.Companion.FAVICON
import com.github.duplicates.bookmark.BookmarkComparator.Companion.TITLE
import com.github.duplicates.bookmark.BookmarkComparator.Companion.URL

/**
 * View holder of a duplicate bookmark.
 *
 * @author moshe.w
 */
class BookmarkViewHolder(itemView: View, onCheckedChangeListener: OnItemCheckedChangeListener<BookmarkItem>? = null) : DuplicateViewHolder<BookmarkItem>(itemView, onCheckedChangeListener) {

    @BindView(R.id.match)
    lateinit var match: TextView

    @BindView(R.id.checkbox1)
    lateinit var checkbox1: CheckBox
    @BindView(R.id.created1)
    lateinit var created1: TextView
    @BindView(R.id.date1)
    lateinit var date1: TextView
    @BindView(R.id.icon1)
    lateinit var icon1: ImageView
    @BindView(R.id.title1)
    lateinit var title1: TextView
    @BindView(R.id.url1)
    lateinit var url1: TextView

    @BindView(R.id.checkbox2)
    lateinit var checkbox2: CheckBox
    @BindView(R.id.created2)
    lateinit var created2: TextView
    @BindView(R.id.date2)
    lateinit var date2: TextView
    @BindView(R.id.icon2)
    lateinit var icon2: ImageView
    @BindView(R.id.title2)
    lateinit var title2: TextView
    @BindView(R.id.url2)
    lateinit var url2: TextView

    init {
        ButterKnife.bind(this, itemView)
    }

    override fun bindHeader(context: Context, pair: DuplicateItemPair<BookmarkItem>) {
        match.text = context.getString(R.string.match, percentFormatter.format(pair.match.toDouble()))
    }

    override fun bindItem1(context: Context, item: BookmarkItem) {
        checkbox1.isChecked = item.isChecked
        checkbox1.text = if (BuildConfig.DEBUG) item.id.toString() else ""
        created1.text = DateUtils.formatDateTime(context, item.created, DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_TIME or DateUtils.FORMAT_ABBREV_WEEKDAY)
        date1.text = DateUtils.formatDateTime(context, item.date, DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_TIME or DateUtils.FORMAT_ABBREV_WEEKDAY)
        icon1.setImageBitmap(item.icon)
        title1.text = item.title
        url1.text = item.uri?.toString()
    }

    override fun bindItem2(context: Context, item: BookmarkItem) {
        checkbox2.isChecked = item.isChecked
        checkbox2.text = if (BuildConfig.DEBUG) item.id.toString() else ""
        created2.text = DateUtils.formatDateTime(context, item.created, DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_TIME or DateUtils.FORMAT_ABBREV_WEEKDAY)
        date2.text = DateUtils.formatDateTime(context, item.date, DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_TIME or DateUtils.FORMAT_ABBREV_WEEKDAY)
        icon2.setImageBitmap(item.icon)
        title2.text = item.title
        url2.text = item.uri?.toString()
    }

    override fun bindDifference(context: Context, pair: DuplicateItemPair<BookmarkItem>) {
        val difference = pair.difference

        bindDifference(created1, created2, difference[CREATED])
        bindDifference(date1, date2, difference[DATE])
        bindDifference(icon1, icon2, difference[FAVICON])
        bindDifference(title1, title2, difference[TITLE])
        bindDifference(url1, url2, difference[URL])
    }

    @OnClick(R.id.checkbox1)
    internal fun checkbox1Clicked() {
        onCheckedChangeListener?.onItemCheckedChangeListener(item1, checkbox1.isChecked)
    }

    @OnClick(R.id.checkbox2)
    internal fun checkbox2Clicked() {
        onCheckedChangeListener?.onItemCheckedChangeListener(item2, checkbox2.isChecked)
    }
}
