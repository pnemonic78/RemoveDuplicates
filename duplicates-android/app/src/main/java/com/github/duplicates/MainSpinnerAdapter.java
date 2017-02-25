/*
 * Source file of the Remove Duplicates project.
 * Copyright (c) 2016. All Rights Reserved.
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/2.0
 *
 * Contributors can be contacted by electronic mail via the project Web pages:
 *
 * https://github.com/pnemonic78/RemoveDuplicates
 *
 * Contributor(s):
 *   Moshe Waisberg
 *
 */
package com.github.duplicates;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

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
        TextView textView = (TextView) convertView.findViewById(android.R.id.text1);
        textView.setText(item.getLabel());
        textView.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(item.getIcon()), null, null, null);
        textView.setCompoundDrawablePadding(context.getResources().getDimensionPixelSize(R.dimen.drawable_padding));

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }

        MainSpinnerItem item = getItem(position);
        TextView textView = (TextView) convertView.findViewById(android.R.id.text1);
        textView.setText(item.getLabel());
        textView.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(item.getIcon()), null, null, null);
        textView.setCompoundDrawablePadding(context.getResources().getDimensionPixelSize(R.dimen.drawable_padding));

        return convertView;
    }
}
