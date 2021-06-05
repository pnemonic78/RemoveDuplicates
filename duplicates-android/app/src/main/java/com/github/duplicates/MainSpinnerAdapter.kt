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

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import com.github.android.removeduplicates.R

/**
 * Spinner adapter for the main activity spinner.
 *
 * @author moshe.w
 */
class MainSpinnerAdapter : BaseAdapter() {

    private val items = MainSpinnerItem.values().filter { it.isVisible }

    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(position: Int): MainSpinnerItem {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return items[position].ordinal.toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(parent.context)
                .inflate(android.R.layout.simple_spinner_item, parent, false)
        }

        bindView(position, view!!)

        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(parent.context)
                .inflate(android.R.layout.simple_spinner_dropdown_item, parent, false)
        }

        bindView(position, view!!)

        return view
    }

    private fun bindView(position: Int, view: View) {
        val item = getItem(position)
        val context = view.context
        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.setText(item.label)
        val icon = AppCompatResources.getDrawable(context, item.icon)
        textView.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null)
        textView.compoundDrawablePadding =
            context.resources.getDimensionPixelSize(R.dimen.drawable_padding)
    }

    override fun isEnabled(position: Int): Boolean {
        return getItem(position).isVisible
    }
}
