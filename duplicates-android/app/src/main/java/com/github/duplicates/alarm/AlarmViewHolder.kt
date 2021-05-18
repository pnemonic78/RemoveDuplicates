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
import android.view.ViewGroup
import com.github.android.removeduplicates.BuildConfig
import com.github.android.removeduplicates.R
import com.github.android.removeduplicates.databinding.SameAlarmBinding
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
class AlarmViewHolder(
    itemView: ViewGroup,
    binding: SameAlarmBinding,
    onCheckedChangeListener: OnItemCheckedChangeListener<AlarmItem>? = null
) : DuplicateViewHolder<AlarmItem>(itemView, onCheckedChangeListener) {

    private val match = binding.match

    private val checkbox1 = binding.checkbox1
    private val alarm1 = binding.alarm1
    private val alert1 = binding.alert1
    private val repeat1 = binding.repeat1
    private val name1 = binding.name1

    private val checkbox2 = binding.checkbox2
    private val alarm2 = binding.alarm2
    private val alert2 = binding.alert2
    private val repeat2 = binding.repeat2
    private val name2 = binding.name2

    init {
        checkbox1.setOnClickListener {
            onCheckedChangeListener?.onItemCheckedChangeListener(item1, checkbox1.isChecked)
        }
        checkbox2.setOnClickListener {
            onCheckedChangeListener?.onItemCheckedChangeListener(item2, checkbox2.isChecked)
        }
    }

    override fun bindHeader(context: Context, pair: DuplicateItemPair<AlarmItem>) {
        match.text =
            context.getString(R.string.match, percentFormatter.format(pair.match.toDouble()))
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
