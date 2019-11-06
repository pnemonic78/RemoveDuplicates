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
import com.github.duplicates.DuplicateItemPair
import com.github.duplicates.DuplicateViewHolder
import com.github.duplicates.SHOW_DATE_TIME
import com.github.duplicates.calendar.CalendarComparator.Companion.DESCRIPTION
import com.github.duplicates.calendar.CalendarComparator.Companion.DTEND
import com.github.duplicates.calendar.CalendarComparator.Companion.DTSTART
import com.github.duplicates.calendar.CalendarComparator.Companion.LOCATION
import com.github.duplicates.calendar.CalendarComparator.Companion.TITLE
import com.github.duplicates.calendar.CalendarItem.Companion.NEVER
import kotlinx.android.synthetic.main.same_calendar.view.*
import kotlin.math.min

/**
 * View holder of a duplicate calendar events.
 *
 * @author moshe.w
 */
class CalendarViewHolder(itemView: View, onCheckedChangeListener: OnItemCheckedChangeListener<CalendarItem>? = null) : DuplicateViewHolder<CalendarItem>(itemView, onCheckedChangeListener) {

    private val match = itemView.match

    private val checkbox1 = itemView.checkbox1
    private val color1 = itemView.color1
    private val start1 = itemView.start1
    private val end1 = itemView.end1
    private val recur1 = itemView.recur1
    private val account1 = itemView.account1
    private val title1 = itemView.title1
    private val description1 = itemView.description1
    private val location1 = itemView.location1

    private val checkbox2 = itemView.checkbox2
    private val color2 = itemView.color2
    private val start2 = itemView.start2
    private val end2 = itemView.end2
    private val recur2 = itemView.recur2
    private val account2 = itemView.account2
    private val title2 = itemView.title2
    private val description2 = itemView.description2
    private val location2 = itemView.location2

    init {
        checkbox1.setOnClickListener {
            onCheckedChangeListener?.onItemCheckedChangeListener(item1, checkbox1.isChecked)
        }
        checkbox2.setOnClickListener {
            onCheckedChangeListener?.onItemCheckedChangeListener(item2, checkbox2.isChecked)
        }
    }

    override fun bindHeader(context: Context, pair: DuplicateItemPair<CalendarItem>) {
        match.text = context.getString(R.string.match, percentFormatter.format(pair.match.toDouble()))
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
