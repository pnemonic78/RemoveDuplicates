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

import android.graphics.Bitmap;
import android.net.Uri;

import java.util.Arrays;
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
     * @param difference the array of differences.
     * @return the match as a percentage between {@code 0.0} (dissimilar) and {@code 1.0} (identical) inclusive.
     */
    public abstract float match(boolean[] difference);

    /**
     * How different are the two items?
     *
     * @param lhs the first item.
     * @param rhs the other item.
     * @return the array of differences.
     */
    public abstract boolean[] difference(T lhs, T rhs);

    /**
     * How similar are the two items?
     *
     * @param lhs the first item.
     * @param rhs the other item.
     * @return the match as a percentage between {@code 0.0} (dissimilar) and {@code 1.0} (identical) inclusive.
     * @see #match(boolean[])
     * @see #difference(DuplicateItem, DuplicateItem)
     */
    public float match(T lhs, T rhs) {
        return match(difference(lhs, rhs));
    }

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

    public static int compare(byte[] lhs, byte[] rhs) {
        if (lhs == null) {
            return (rhs == null) ? SAME : RHS;
        }
        if (rhs == null) {
            return LHS;
        }
        int l1 = lhs.length;
        int l2 = rhs.length;
        int c = l1 - l2;
        if ((c == 0) && Arrays.equals(lhs, rhs)) {
            return SAME;
        }
        return c;
    }

    public static int compare(Bitmap lhs, Bitmap rhs) {
        if (lhs == null) {
            return (rhs == null) ? SAME : RHS;
        }
        if (rhs == null) {
            return LHS;
        }
        int w1 = lhs.getWidth();
        int w2 = rhs.getWidth();
        int c = w1 - w2;
        if (c != 0) {
            return c;
        }
        int h1 = lhs.getHeight();
        int h2 = rhs.getHeight();
        c = h1 - h2;
        if ((c == 0) && lhs.sameAs(rhs)) {
            return SAME;
        }
        return c;
    }

    public static int compare(Uri lhs, Uri rhs) {
        if (lhs == null) {
            return (rhs == null) ? SAME : RHS;
        }
        if (rhs == null) {
            return LHS;
        }
        String s1 = lhs.toString();
        if (s1.endsWith("/")) {
            s1 = s1.substring(0, s1.length() - 1);
        }
        String s2 = rhs.toString();
        if (s2.endsWith("/")) {
            s2 = s2.substring(0, s2.length() - 1);
        }
        return s1.compareTo(s2);
    }
}
