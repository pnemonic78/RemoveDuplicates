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
class CalendarViewHolder(itemView: View, onCheckedChangeListener: DuplicateViewHolder.OnItemCheckedChangeListener<*>? = null) : DuplicateViewHolder<CalendarItem>(itemView, onCheckedChangeListener) {

    @BindView(R.id.match)
    lateinit var match: TextView

    @BindView(R.id.checkbox1)
    lateinit var checkbox1: CheckBox
    @BindView(R.id.color1)
    lateinit var color1: View
    @BindView(R.id.start1)
    lateinit var start1: TextView
    @BindView(R.id.end1)
    lateinit var end1: TextView
    @BindView(R.id.recur1)
    lateinit var recur1: ImageView
    @BindView(R.id.account1)
    lateinit var account1: TextView
    @BindView(R.id.title1)
    lateinit var title1: TextView
    @BindView(R.id.description1)
    lateinit var description1: TextView
    @BindView(R.id.location1)
    lateinit var location1: TextView

    @BindView(R.id.checkbox2)
    lateinit var checkbox2: CheckBox
    @BindView(R.id.color2)
    lateinit var color2: View
    @BindView(R.id.start2)
    lateinit var start2: TextView
    @BindView(R.id.end2)
    lateinit var end2: TextView
    @BindView(R.id.recur2)
    lateinit var recur2: ImageView
    @BindView(R.id.account2)
    lateinit var account2: TextView
    @BindView(R.id.title2)
    lateinit var title2: TextView
    @BindView(R.id.description2)
    lateinit var description2: TextView
    @BindView(R.id.location2)
    lateinit var location2: TextView

    init {
        ButterKnife.bind(this, itemView)
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
            DateUtils.formatDateTime(context, date, DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_TIME or DateUtils.FORMAT_ABBREV_WEEKDAY)
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

    @OnClick(R.id.checkbox1)
    internal fun checkbox1Clicked() {
        onCheckedChangeListener?.onItemCheckedChangeListener(item1, checkbox1.isChecked)
    }

    @OnClick(R.id.checkbox2)
    internal fun checkbox2Clicked() {
        onCheckedChangeListener?.onItemCheckedChangeListener(item2, checkbox2.isChecked)
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
