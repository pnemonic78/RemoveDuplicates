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
package com.github.duplicates.call

import android.content.Context
import android.text.format.DateUtils
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.github.android.removeduplicates.BuildConfig
import com.github.android.removeduplicates.R
import com.github.duplicates.DuplicateItemPair
import com.github.duplicates.DuplicateViewHolder
import com.github.duplicates.SHOW_DATE_TIME
import com.github.duplicates.call.CallLogComparator.Companion.DATE
import com.github.duplicates.call.CallLogComparator.Companion.DURATION
import com.github.duplicates.call.CallLogComparator.Companion.NAME
import com.github.duplicates.call.CallLogComparator.Companion.NUMBER
import com.github.duplicates.call.CallLogComparator.Companion.TYPE
import com.github.duplicates.call.CallLogItem.Companion.ANSWERED_EXTERNALLY_TYPE
import com.github.duplicates.call.CallLogItem.Companion.BLOCKED_TYPE
import com.github.duplicates.call.CallLogItem.Companion.INCOMING_TYPE
import com.github.duplicates.call.CallLogItem.Companion.MISSED_TYPE
import com.github.duplicates.call.CallLogItem.Companion.OUTGOING_TYPE
import com.github.duplicates.call.CallLogItem.Companion.REJECTED_TYPE
import com.github.duplicates.call.CallLogItem.Companion.VOICEMAIL_TYPE
import kotlinx.android.synthetic.main.same_call.view.*

/**
 * View holder of a duplicate call.
 *
 * @author moshe.w
 */
class CallLogViewHolder(itemView: View, onCheckedChangeListener: OnItemCheckedChangeListener<CallLogItem>? = null) : DuplicateViewHolder<CallLogItem>(itemView, onCheckedChangeListener) {

    private val match = itemView.match

    private val checkbox1 = itemView.checkbox1
    private val date1 = itemView.date1
    private val duration1 = itemView.duration1
    private val number1 = itemView.number1
    private val type1 = itemView.type1
    private val name1 = itemView.name1

    private val checkbox2 = itemView.checkbox2
    private val date2 = itemView.date2
    private val duration2 = itemView.duration2
    private val number2 = itemView.number2
    private val type2 = itemView.type2
    private val name2 = itemView.name1

    init {
        checkbox1.setOnClickListener {
            onCheckedChangeListener?.onItemCheckedChangeListener(item1, checkbox1.isChecked)
        }
        checkbox2.setOnClickListener {
            onCheckedChangeListener?.onItemCheckedChangeListener(item2, checkbox2.isChecked)
        }
    }

    override fun bindHeader(context: Context, pair: DuplicateItemPair<CallLogItem>) {
        match.text = context.getString(R.string.match, percentFormatter.format(pair.match.toDouble()))
    }

    override fun bindItem1(context: Context, item: CallLogItem) {
        checkbox1.isChecked = item.isChecked
        checkbox1.text = if (BuildConfig.DEBUG) item.id.toString() else ""
        date1.text = DateUtils.formatDateTime(context, item.date, SHOW_DATE_TIME)
        duration1.text = DateUtils.formatElapsedTime(item.duration)
        type1.setImageResource(getTypeIcon(item.type))
        type1.contentDescription = context.getText(getTypeName(item.type))
        number1.text = item.number
        name1.text = item.name
    }

    override fun bindItem2(context: Context, item: CallLogItem) {
        checkbox2.isChecked = item.isChecked
        checkbox2.text = if (BuildConfig.DEBUG) item.id.toString() else ""
        date2.text = DateUtils.formatDateTime(context, item.date, SHOW_DATE_TIME)
        duration2.text = DateUtils.formatElapsedTime(item.duration)
        type2.setImageResource(getTypeIcon(item.type))
        type2.contentDescription = context.getText(getTypeName(item.type))
        number2.text = item.number
        name2.text = item.name
    }

    override fun bindDifference(context: Context, pair: DuplicateItemPair<CallLogItem>) {
        val difference = pair.difference

        bindDifference(date1, date2, difference[DATE])
        bindDifference(duration1, duration2, difference[DURATION])
        bindDifference(type1, type2, difference[TYPE])
        bindDifference(number1, number2, difference[NUMBER])
        bindDifference(name1, name2, difference[NAME])
    }

    @DrawableRes
    private fun getTypeIcon(type: Int): Int {
        return when (type) {
            INCOMING_TYPE -> R.drawable.ic_call_received
            OUTGOING_TYPE -> R.drawable.ic_call_made
            MISSED_TYPE -> R.drawable.ic_call_missed
            VOICEMAIL_TYPE -> R.drawable.ic_voicemail
            REJECTED_TYPE -> R.drawable.ic_cancel
            BLOCKED_TYPE -> R.drawable.ic_block
            ANSWERED_EXTERNALLY_TYPE -> R.drawable.ic_devices_other
            else -> R.drawable.ic_call
        }
    }

    @StringRes
    private fun getTypeName(type: Int): Int {
        return when (type) {
            INCOMING_TYPE -> R.string.call_type_incoming
            OUTGOING_TYPE -> R.string.call_type_outgoing
            MISSED_TYPE -> R.string.call_type_missed
            VOICEMAIL_TYPE -> R.string.call_type_voicemail
            REJECTED_TYPE -> R.string.call_type_rejected
            BLOCKED_TYPE -> R.string.call_type_blocked
            ANSWERED_EXTERNALLY_TYPE -> R.string.call_type_external
            else -> R.string.call_type_other
        }
    }
}
