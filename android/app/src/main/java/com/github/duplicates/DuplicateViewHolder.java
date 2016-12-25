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

import android.content.res.ColorStateList;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.android.removeduplicates.R;

/**
 * View holder for a duplicate item.
 *
 * @author moshe.w
 */
public abstract class DuplicateViewHolder<T extends DuplicateItem> extends RecyclerView.ViewHolder {

    protected final ColorStateList colorDifferent;

    public DuplicateViewHolder(View itemView) {
        super(itemView);
        colorDifferent = ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.different));
    }

    public abstract void bind(DuplicateItemPair<T> pair);
}
