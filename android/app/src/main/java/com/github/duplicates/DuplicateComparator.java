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

    public static final int SAME = 0;
    protected static final int LHS = +1;
    protected static final int RHS = -1;

    @Override
    public int compare(T lhs, T rhs) {
        return compare(lhs.getId(), rhs.getId());
    }

    /**
     * How similar are the two items?
     *
     * @param lhs the first item.
     * @param rhs the other item.
     * @return the match as a percentage between {@code 0.0} (dissimilar) and {@code 1.0} (identical) inclusive.
     */
    public abstract float match(T lhs, T rhs);

    public static int compare(int lhs, int rhs) {
        return lhs - rhs;
    }

    public static int compare(long lhs, long rhs) {
        return (lhs > rhs) ? LHS : ((lhs < rhs) ? RHS : SAME);
    }

    public static int compare(String lhs, String rhs) {
        return (lhs == null) ? ((rhs == null) ? SAME : RHS) : ((rhs == null) ? LHS : lhs.compareTo(rhs));
    }

    public static int compare(boolean lhs, boolean rhs) {
        return lhs == rhs ? SAME : (lhs ? LHS : RHS);
    }
}
