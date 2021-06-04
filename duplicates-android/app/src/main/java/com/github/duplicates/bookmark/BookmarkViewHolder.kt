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
import android.view.ViewGroup
import com.github.android.removeduplicates.BuildConfig
import com.github.android.removeduplicates.R
import com.github.android.removeduplicates.databinding.SameBookmarkBinding
import com.github.duplicates.DuplicateItemPair
import com.github.duplicates.DuplicateViewHolder
import com.github.duplicates.SHOW_DATE_TIME
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
class BookmarkViewHolder(
    itemView: ViewGroup,
    binding: SameBookmarkBinding,
    onCheckedChangeListener: OnItemCheckedChangeListener<BookmarkItem>? = null
) : DuplicateViewHolder<BookmarkItem>(itemView, onCheckedChangeListener) {

    private val match = binding.match

    private val checkbox1 = binding.item1.checkbox
    private val created1 = binding.item1.created
    private val date1 = binding.item1.date
    private val icon1 = binding.item1.icon
    private val title1 = binding.item1.title
    private val url1 = binding.item1.url

    private val checkbox2 = binding.item2.checkbox
    private val created2 = binding.item2.created
    private val date2 = binding.item2.date
    private val icon2 = binding.item2.icon
    private val title2 = binding.item2.title
    private val url2 = binding.item2.url

    init {
        checkbox1.setOnClickListener {
            onCheckedChangeListener?.onItemCheckedChangeListener(item1, checkbox1.isChecked)
        }
        checkbox2.setOnClickListener {
            onCheckedChangeListener?.onItemCheckedChangeListener(item2, checkbox2.isChecked)
        }
    }

    override fun bindHeader(context: Context, pair: DuplicateItemPair<BookmarkItem>) {
        match.text =
            context.getString(R.string.match, percentFormatter.format(pair.match.toDouble()))
    }

    override fun bindItem1(context: Context, item: BookmarkItem) {
        checkbox1.isChecked = item.isChecked
        checkbox1.text = if (BuildConfig.DEBUG) item.id.toString() else ""
        created1.text = DateUtils.formatDateTime(context, item.created, SHOW_DATE_TIME)
        date1.text = DateUtils.formatDateTime(context, item.date, SHOW_DATE_TIME)
        icon1.setImageBitmap(item.icon)
        title1.text = item.title
        url1.text = item.uri?.toString()
    }

    override fun bindItem2(context: Context, item: BookmarkItem) {
        checkbox2.isChecked = item.isChecked
        checkbox2.text = if (BuildConfig.DEBUG) item.id.toString() else ""
        created2.text = DateUtils.formatDateTime(context, item.created, SHOW_DATE_TIME)
        date2.text = DateUtils.formatDateTime(context, item.date, SHOW_DATE_TIME)
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
}
