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

import java.util.Comparator;

/**
 * Comparator to determine if it is a duplicate.
 *
 * @author moshe.w
 */
public abstract class DuplicateComparator<T extends DuplicateItem> implements Comparator<T> {

    protected static final int MAX_LEVEL = 10000;

    @Override
    public int compare(T lhs, T rhs) {
        return MAX_LEVEL - similar(lhs, rhs);
    }

    /**
     * How similar are the two items?
     *
     * @param lhs the first item.
     * @param rhs the other item.
     * @return the similarity, on a scale of {@code 0} (completely dissimilar) to {@code {@link #MAX_LEVEL}} (definitely similar).
     */
    protected abstract int similar(T lhs, T rhs);
}
