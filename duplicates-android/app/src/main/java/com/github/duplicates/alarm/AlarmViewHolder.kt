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
package com.github.duplicates.alarm

import android.content.Context
import android.text.format.DateFormat
import android.text.format.DateUtils
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.github.android.removeduplicates.BuildConfig
import com.github.android.removeduplicates.R
import com.github.duplicates.DuplicateItemPair
import com.github.duplicates.DuplicateViewHolder
import com.github.duplicates.alarm.AlarmComparator.Companion.ALARM_TIME
import com.github.duplicates.alarm.AlarmComparator.Companion.ALERT_TIME
import com.github.duplicates.alarm.AlarmComparator.Companion.NAME
import com.github.duplicates.alarm.AlarmComparator.Companion.REPEAT
import java.util.*

/**
 * View holder of a duplicate alarms.
 *
 * @author moshe.w
 */
class AlarmViewHolder(itemView: View, onCheckedChangeListener: OnItemCheckedChangeListener<AlarmItem>? = null) : DuplicateViewHolder<AlarmItem>(itemView, onCheckedChangeListener) {

    @BindView(R.id.match)
    lateinit var match: TextView

    @BindView(R.id.checkbox1)
    lateinit var checkbox1: CheckBox
    @BindView(R.id.alarm1)
    lateinit var alarm1: TextView
    @BindView(R.id.alert1)
    lateinit var alert1: TextView
    @BindView(R.id.repeat1)
    lateinit var repeat1: TextView
    @BindView(R.id.name1)
    lateinit var name1: TextView

    @BindView(R.id.checkbox2)
    lateinit var checkbox2: CheckBox
    @BindView(R.id.alarm2)
    lateinit var alarm2: TextView
    @BindView(R.id.alert2)
    lateinit var alert2: TextView
    @BindView(R.id.repeat2)
    lateinit var repeat2: TextView
    @BindView(R.id.name2)
    lateinit var name2: TextView

    init {
        ButterKnife.bind(this, itemView)
    }

    override fun bindHeader(context: Context, pair: DuplicateItemPair<AlarmItem>) {
        match.text = context.getString(R.string.match, percentFormatter.format(pair.match.toDouble()))
    }

    override fun bindItem1(context: Context, item: AlarmItem) {
        checkbox1.isChecked = item.isChecked
        checkbox1.text = if (BuildConfig.DEBUG) item.id.toString() else ""
        alarm1.text = formatHourMinutes(context, item.alarmTime)
        alert1.text = DateUtils.formatDateTime(context, item.alertTime, DateUtils.FORMAT_SHOW_TIME)
        name1.text = item.name
        repeat1.text = formatRepeat(context, item)
    }

    override fun bindItem2(context: Context, item: AlarmItem) {
        checkbox2.isChecked = item.isChecked
        checkbox2.text = if (BuildConfig.DEBUG) item.id.toString() else ""
        alarm2.text = formatHourMinutes(context, item.alarmTime)
        alert2.text = DateUtils.formatDateTime(context, item.alertTime, DateUtils.FORMAT_SHOW_TIME)
        name2.text = item.name
        repeat2.text = formatRepeat(context, item)
    }

    override fun bindDifference(context: Context, pair: DuplicateItemPair<AlarmItem>) {
        val difference = pair.difference

        bindDifference(alarm1, alarm2, difference[ALARM_TIME])
        bindDifference(alert1, alert2, difference[ALERT_TIME])
        bindDifference(name1, name2, difference[NAME])
        bindDifference(repeat1, repeat2, difference[REPEAT])
    }

    @OnClick(R.id.checkbox1)
    internal fun checkbox1Clicked() {
        onCheckedChangeListener?.onItemCheckedChangeListener(item1, checkbox1.isChecked)
    }

    @OnClick(R.id.checkbox2)
    internal fun checkbox2Clicked() {
        onCheckedChangeListener?.onItemCheckedChangeListener(item2, checkbox2.isChecked)
    }

    private fun formatHourMinutes(context: Context, alarmTime: Int): CharSequence {
        val hours = (alarmTime / 100).toLong()
        val minutes = (alarmTime % 100).toLong()
        val time = hours * DateUtils.HOUR_IN_MILLIS + minutes * DateUtils.MINUTE_IN_MILLIS
        val format = DateFormat.getTimeFormat(context)
        format.timeZone = TimeZone.getTimeZone("UTC")
        return format.format(Date(time))
    }

    private fun formatRepeat(context: Context, item: AlarmItem): CharSequence {
        return item.repeat?.toString(context, true) ?: ""
    }
}
