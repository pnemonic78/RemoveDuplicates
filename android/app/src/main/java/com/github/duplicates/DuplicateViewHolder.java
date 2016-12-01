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

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * View holder for a duplicate item.
 *
 * @author moshe.w
 */
public abstract class DuplicateViewHolder<T extends DuplicateItem> extends RecyclerView.ViewHolder {

    public DuplicateViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void bind(DuplicateItemPair<T> pair);
}
