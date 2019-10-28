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
package com.github.duplicates;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;

import com.github.android.removeduplicates.R;

/**
 * Spinner adapter for the main activity spinner.
 *
 * @author moshe.w
 */
public class MainSpinnerAdapter extends BaseAdapter {

    private final MainSpinnerItem[] items = MainSpinnerItem.values();

    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public MainSpinnerItem getItem(int position) {
        return items[position];
    }

    @Override
    public long getItemId(int position) {
        return items[position].ordinal();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_spinner_item, parent, false);
        }

        MainSpinnerItem item = getItem(position);
        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(item.label);
        textView.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(item.icon), null, null, null);
        textView.setCompoundDrawablePadding(context.getResources().getDimensionPixelSize(R.dimen.drawable_padding));
        convertView.setEnabled(item.enabled);

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }

        MainSpinnerItem item = getItem(position);
        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(item.label);
        Drawable icon = AppCompatResources.getDrawable(context, item.icon);
        textView.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
        textView.setCompoundDrawablePadding(context.getResources().getDimensionPixelSize(R.dimen.drawable_padding));
        convertView.setEnabled(item.enabled);

        return convertView;
    }

    @Override
    public boolean isEnabled(int position) {
        return getItem(position).enabled;
    }
}
