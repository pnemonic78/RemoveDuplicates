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
package com.github.duplicates.message

import android.content.Context
import android.text.format.DateUtils
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

import com.github.android.removeduplicates.BuildConfig
import com.github.android.removeduplicates.R
import com.github.duplicates.DuplicateItemPair
import com.github.duplicates.DuplicateViewHolder

import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.github.duplicates.SHOW_DATE_TIME

import com.github.duplicates.message.MessageComparator.Companion.ADDRESS
import com.github.duplicates.message.MessageComparator.Companion.BODY
import com.github.duplicates.message.MessageComparator.Companion.DATE
import com.github.duplicates.message.MessageComparator.Companion.TYPE
import com.github.duplicates.message.MessageItem.Companion.MESSAGE_TYPE_ALL
import com.github.duplicates.message.MessageItem.Companion.MESSAGE_TYPE_DRAFT
import com.github.duplicates.message.MessageItem.Companion.MESSAGE_TYPE_FAILED
import com.github.duplicates.message.MessageItem.Companion.MESSAGE_TYPE_INBOX
import com.github.duplicates.message.MessageItem.Companion.MESSAGE_TYPE_OUTBOX
import com.github.duplicates.message.MessageItem.Companion.MESSAGE_TYPE_QUEUED
import com.github.duplicates.message.MessageItem.Companion.MESSAGE_TYPE_SENT

/**
 * View holder of a duplicate message.
 *
 * @author moshe.w
 */
class MessageViewHolder(itemView: View, onCheckedChangeListener: OnItemCheckedChangeListener<MessageItem>) : DuplicateViewHolder<MessageItem>(itemView, onCheckedChangeListener) {

    @BindView(R.id.match)
    lateinit var match: TextView

    @BindView(R.id.checkbox1)
    lateinit var checkbox1: CheckBox
    @BindView(R.id.date1)
    lateinit var date1: TextView
    @BindView(R.id.address1)
    lateinit var address1: TextView
    @BindView(R.id.type1)
    lateinit var type1: ImageView
    @BindView(R.id.body1)
    lateinit var body1: TextView

    @BindView(R.id.checkbox2)
    lateinit var checkbox2: CheckBox
    @BindView(R.id.date2)
    lateinit var date2: TextView
    @BindView(R.id.address2)
    lateinit var address2: TextView
    @BindView(R.id.type2)
    lateinit var type2: ImageView
    @BindView(R.id.body2)
    lateinit var body2: TextView

    init {
        ButterKnife.bind(this, itemView)
    }

    override fun bindHeader(context: Context, pair: DuplicateItemPair<MessageItem>) {
        match.text = context.getString(R.string.match, percentFormatter.format(pair.match.toDouble()))
    }

    override fun bindItem1(context: Context, item: MessageItem) {
        checkbox1.isChecked = item.isChecked
        checkbox1.text = if (BuildConfig.DEBUG) item.id.toString() else ""
        date1.text = DateUtils.formatDateTime(context, item.dateReceived, SHOW_DATE_TIME)
        address1.text = item.address
        type1.setImageResource(getTypeIcon(item.type))
        type1.contentDescription = context.getText(getTypeName(item.type))
        body1.text = item.body
    }

    override fun bindItem2(context: Context, item: MessageItem) {
        checkbox2.isChecked = item.isChecked
        checkbox2.text = if (BuildConfig.DEBUG) item.id.toString() else ""
        date2.text = DateUtils.formatDateTime(context, item.dateReceived, SHOW_DATE_TIME)
        address2.text = item.address
        type2.setImageResource(getTypeIcon(item.type))
        type2.contentDescription = context.getText(getTypeName(item.type))
        body2.text = item.body
    }

    override fun bindDifference(context: Context, pair: DuplicateItemPair<MessageItem>) {
        val difference = pair.difference

        bindDifference(date1, date2, difference[DATE])
        bindDifference(address1, address2, difference[ADDRESS])
        bindDifference(type1, type2, difference[TYPE])
        bindDifference(body1, body2, difference[BODY])
    }

    @DrawableRes
    private fun getTypeIcon(type: Int): Int {
        return when (type) {
            MESSAGE_TYPE_DRAFT -> R.drawable.ic_drafts
            MESSAGE_TYPE_FAILED -> R.drawable.ic_cancel
            MESSAGE_TYPE_INBOX -> R.drawable.ic_inbox
            MESSAGE_TYPE_OUTBOX -> R.drawable.ic_outbox
            MESSAGE_TYPE_QUEUED -> R.drawable.ic_queue
            MESSAGE_TYPE_SENT -> R.drawable.ic_send
            MESSAGE_TYPE_ALL -> R.drawable.ic_message
            else -> R.drawable.ic_message
        }
    }

    @StringRes
    private fun getTypeName(type: Int): Int {
        return when (type) {
            MESSAGE_TYPE_DRAFT -> R.string.message_type_drafts
            MESSAGE_TYPE_FAILED -> R.string.message_type_failed
            MESSAGE_TYPE_INBOX -> R.string.message_type_inbox
            MESSAGE_TYPE_OUTBOX -> R.string.message_type_outbox
            MESSAGE_TYPE_QUEUED -> R.string.message_type_queued
            MESSAGE_TYPE_SENT -> R.string.message_type_sent
            MESSAGE_TYPE_ALL -> R.string.message_type_all
            else -> R.string.message_type_all
        }
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
