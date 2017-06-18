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

import static com.github.duplicates.DuplicateTaskListener.MATCH_GREAT;

/**
 * Item that is a possible duplicate of two items.
 *
 * @author moshe.w
 */
public class DuplicateItemPair<T extends DuplicateItem> {

    private T item1;
    private T item2;
    private float match;
    private boolean[] difference;

    public DuplicateItemPair() {
    }

    public DuplicateItemPair(T item1, T item2) {
        this(item1, item2, 1f, null);
    }

    public DuplicateItemPair(T item1, T item2, float match, boolean[] difference) {
        this.item1 = item1;
        this.item2 = item2;
        this.match = match;
        this.difference = difference;

        if (match >= MATCH_GREAT) {
            item2.setChecked(true);
        }
    }

    public T getItem1() {
        return item1;
    }

    public T getItem2() {
        return item2;
    }

    public float getMatch() {
        return match;
    }

    public boolean[] getDifference() {
        return difference;
    }

    public long getId() {
        long id1 = getItem1().getId();
        long id2 = getItem2().getId();
        return ((id1 & 0xFFFFFFFFL) << 32) | (id2 & 0xFFFFFFFFL);
    }
}
