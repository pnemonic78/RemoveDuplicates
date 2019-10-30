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
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.github.android.removeduplicates.BuildConfig
import com.github.android.removeduplicates.R
import com.github.duplicates.DuplicateItemPair
import com.github.duplicates.DuplicateViewHolder
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

/**
 * View holder of a duplicate call.
 *
 * @author moshe.w
 */
class CallLogViewHolder @JvmOverloads constructor(itemView: View, onCheckedChangeListener: OnItemCheckedChangeListener<*>? = null) : DuplicateViewHolder<CallLogItem>(itemView, onCheckedChangeListener) {

    @BindView(R.id.match)
    lateinit var match: TextView

    @BindView(R.id.checkbox1)
    lateinit var checkbox1: CheckBox
    @BindView(R.id.date1)
    lateinit var date1: TextView
    @BindView(R.id.duration1)
    lateinit var duration1: TextView
    @BindView(R.id.number1)
    lateinit var number1: TextView
    @BindView(R.id.type1)
    lateinit var type1: ImageView
    @BindView(R.id.name1)
    lateinit var name1: TextView

    @BindView(R.id.checkbox2)
    lateinit var checkbox2: CheckBox
    @BindView(R.id.date2)
    lateinit var date2: TextView
    @BindView(R.id.duration2)
    lateinit var duration2: TextView
    @BindView(R.id.number2)
    lateinit var number2: TextView
    @BindView(R.id.type2)
    lateinit var type2: ImageView
    @BindView(R.id.name2)
    lateinit var name2: TextView

    init {
        ButterKnife.bind(this, itemView)
    }

    override fun bindHeader(context: Context, pair: DuplicateItemPair<CallLogItem>) {
        match.text = context.getString(R.string.match, percentFormatter.format(pair.match.toDouble()))
    }

    override fun bindItem1(context: Context, item: CallLogItem) {
        checkbox1.isChecked = item.isChecked
        checkbox1.text = if (BuildConfig.DEBUG) item.id.toString() else ""
        date1.text = DateUtils.formatDateTime(context, item.date, DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_TIME or DateUtils.FORMAT_ABBREV_WEEKDAY)
        duration1.text = DateUtils.formatElapsedTime(item.duration)
        type1.setImageResource(getTypeIcon(item.type))
        type1.contentDescription = context.getText(getTypeName(item.type))
        number1.text = item.number
        name1.text = item.name
    }

    override fun bindItem2(context: Context, item: CallLogItem) {
        checkbox2.isChecked = item.isChecked
        checkbox2.text = if (BuildConfig.DEBUG) item.id.toString() else ""
        date2.text = DateUtils.formatDateTime(context, item.date, DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_TIME or DateUtils.FORMAT_ABBREV_WEEKDAY)
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
        when (type) {
            INCOMING_TYPE -> return R.drawable.ic_call_received
            OUTGOING_TYPE -> return R.drawable.ic_call_made
            MISSED_TYPE -> return R.drawable.ic_call_missed
            VOICEMAIL_TYPE -> return R.drawable.ic_voicemail
            REJECTED_TYPE -> return R.drawable.ic_cancel
            BLOCKED_TYPE -> return R.drawable.ic_block
            ANSWERED_EXTERNALLY_TYPE -> return R.drawable.ic_devices_other
            else -> return R.drawable.ic_call
        }
    }

    @StringRes
    private fun getTypeName(type: Int): Int {
        when (type) {
            INCOMING_TYPE -> return R.string.call_type_incoming
            OUTGOING_TYPE -> return R.string.call_type_outgoing
            MISSED_TYPE -> return R.string.call_type_missed
            VOICEMAIL_TYPE -> return R.string.call_type_voicemail
            REJECTED_TYPE -> return R.string.call_type_rejected
            BLOCKED_TYPE -> return R.string.call_type_blocked
            ANSWERED_EXTERNALLY_TYPE -> return R.string.call_type_external
            else -> return R.string.call_type_other
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
