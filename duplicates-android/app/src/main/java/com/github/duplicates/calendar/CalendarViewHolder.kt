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
package com.github.duplicates.calendar

import android.content.Context
import android.graphics.Color
import android.text.format.DateUtils
import android.view.View
import com.github.android.removeduplicates.BuildConfig
import com.github.android.removeduplicates.R
import com.github.android.removeduplicates.databinding.SameCalendarBinding
import com.github.duplicates.DuplicateItemPair
import com.github.duplicates.DuplicateViewHolder
import com.github.duplicates.SHOW_DATE_TIME
import com.github.duplicates.calendar.CalendarComparator.Companion.DESCRIPTION
import com.github.duplicates.calendar.CalendarComparator.Companion.DTEND
import com.github.duplicates.calendar.CalendarComparator.Companion.DTSTART
import com.github.duplicates.calendar.CalendarComparator.Companion.LOCATION
import com.github.duplicates.calendar.CalendarComparator.Companion.TITLE
import com.github.duplicates.calendar.CalendarItem.Companion.NEVER
import kotlin.math.min

/**
 * View holder of a duplicate calendar events.
 *
 * @author moshe.w
 */
class CalendarViewHolder(
    binding: SameCalendarBinding,
    onCheckedChangeListener: OnItemCheckedChangeListener<CalendarItem>? = null
) : DuplicateViewHolder<CalendarItem>(binding.root, onCheckedChangeListener) {

    private val match = binding.match

    private val checkbox1 = binding.checkbox1
    private val color1 = binding.color1
    private val start1 = binding.start1
    private val end1 = binding.end1
    private val recur1 = binding.recur1
    private val account1 = binding.account1
    private val title1 = binding.title1
    private val description1 = binding.description1
    private val location1 = binding.location1

    private val checkbox2 = binding.checkbox2
    private val color2 = binding.color2
    private val start2 = binding.start2
    private val end2 = binding.end2
    private val recur2 = binding.recur2
    private val account2 = binding.account2
    private val title2 = binding.title2
    private val description2 = binding.description2
    private val location2 = binding.location2

    init {
        checkbox1.setOnClickListener {
            onCheckedChangeListener?.onItemCheckedChangeListener(item1, checkbox1.isChecked)
        }
        checkbox2.setOnClickListener {
            onCheckedChangeListener?.onItemCheckedChangeListener(item2, checkbox2.isChecked)
        }
    }

    override fun bindHeader(context: Context, pair: DuplicateItemPair<CalendarItem>) {
        match.text =
            context.getString(R.string.match, percentFormatter.format(pair.match.toDouble()))
    }

    override fun bindItem1(context: Context, item: CalendarItem) {
        checkbox1.isChecked = item.isChecked
        checkbox1.text = if (BuildConfig.DEBUG) item.id.toString() else ""
        start1.text = formatDateTime(context, item.start)
        end1.text = formatDateTime(context, item.endEffective)
        recur1.visibility = if (item.hasRecurrence()) View.VISIBLE else View.INVISIBLE
        account1.text = item.calendar.displayName
        title1.text = item.title
        description1.text = item.description
        location1.text = item.location
        color1.setBackgroundColor(getDisplayColorFromColor(item.displayColor))
    }

    override fun bindItem2(context: Context, item: CalendarItem) {
        checkbox2.isChecked = item.isChecked
        checkbox2.text = if (BuildConfig.DEBUG) item.id.toString() else ""
        start2.text = formatDateTime(context, item.start)
        end2.text = formatDateTime(context, item.endEffective)
        recur2.visibility = if (item.hasRecurrence()) View.VISIBLE else View.INVISIBLE
        account2.text = item.calendar.displayName
        title2.text = item.title
        description2.text = item.description
        location2.text = item.location
        color2.setBackgroundColor(getDisplayColorFromColor(item.displayColor))
    }

    private fun formatDateTime(context: Context, date: Long): CharSequence? {
        return if (date == NEVER) {
            null
        } else {
            DateUtils.formatDateTime(context, date, SHOW_DATE_TIME)
        }
    }

    override fun bindDifference(context: Context, pair: DuplicateItemPair<CalendarItem>) {
        val difference = pair.difference

        bindDifference(description1, description2, difference[DESCRIPTION])
        bindDifference(start1, start2, difference[DTSTART])
        bindDifference(end1, end2, difference[DTEND])
        bindDifference(location1, location2, difference[LOCATION])
        bindDifference(title1, title2, difference[TITLE])
    }

    companion object {

        private const val SATURATION_ADJUST = 1.3f
        private const val INTENSITY_ADJUST = 0.8f

        private val hsv = FloatArray(3)

        /**
         * For devices with Jellybean or later, darkens the given color to ensure that white text is
         * clearly visible on top of it.  For devices prior to Jellybean, does nothing, as the
         * sync adapter handles the color change.
         *
         * @param color the raw color.
         */
        fun getDisplayColorFromColor(color: Int): Int {
            Color.colorToHSV(color, hsv)
            hsv[1] = min(hsv[1] * SATURATION_ADJUST, 1.0f)
            hsv[2] = hsv[2] * INTENSITY_ADJUST
            return Color.HSVToColor(hsv)
        }
    }
}
