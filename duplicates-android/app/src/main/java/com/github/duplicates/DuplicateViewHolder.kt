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
package com.github.duplicates

import android.content.Context
import android.graphics.Color
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.github.android.removeduplicates.R
import java.text.NumberFormat

/**
 * View holder for a duplicate item.
 *
 * @author moshe.w
 */
abstract class DuplicateViewHolder<T : DuplicateItem>(itemView: View, protected val onCheckedChangeListener: OnItemCheckedChangeListener<T>? = null) : RecyclerView.ViewHolder(itemView) {

    private val context: Context
    @ColorInt
    protected val colorDifferent: Int
    @ColorInt
    protected val colorError: Int
    protected val percentFormatter = NumberFormat.getPercentInstance()

    protected lateinit var item1: T
    protected lateinit var item2: T

    init {
        val context = itemView.context
        val res = context.resources
        this.context = context
        this.colorDifferent = ResourcesCompat.getColor(res, R.color.different, null)
        this.colorError = ResourcesCompat.getColor(res, R.color.error, null)
    }

    /**
     * Bind the pair to the view.
     *
     * @param pair the pair of items.
     */
    fun bind(pair: DuplicateItemPair<T>) {
        val item1 = pair.item1
        val item2 = pair.item2
        this.item1 = pair.item1
        this.item2 = pair.item2
        bindHeader(context, pair)
        bindItem1(context, item1)
        bindItem2(context, item2)
        bindDifference(context, pair)

        if (item1.isError || item2.isError) {
            itemView.setBackgroundColor(colorError)
        } else {
            itemView.background = null
        }
    }

    protected abstract fun bindHeader(context: Context, pair: DuplicateItemPair<T>)

    protected abstract fun bindItem1(context: Context, item: T)

    protected abstract fun bindItem2(context: Context, item: T)

    protected abstract fun bindDifference(context: Context, pair: DuplicateItemPair<T>)

    protected fun bindDifference(view1: View, view2: View, different: Boolean) {
        if (different) {
            view1.setBackgroundColor(colorDifferent)
            view2.setBackgroundColor(colorDifferent)
        } else {
            view1.setBackgroundColor(Color.TRANSPARENT)
            view2.setBackgroundColor(Color.TRANSPARENT)
        }
    }

    interface OnItemCheckedChangeListener<T> {
        /**
         * Notification that the item was clicked.
         *
         * @param item    the item.
         * @param checked is checked?
         */
        fun onItemCheckedChangeListener(item: T, checked: Boolean)
    }
}