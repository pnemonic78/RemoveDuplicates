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

    @Override
    public int compare(T lhs, T rhs) {
        return similar(lhs, rhs);
    }

    /**
     * How similar are the two items?<p>
     * Up to 32 different attributes can be compared.
     * The least-significant attributes are located in the least-significant bits.<br>
     * When the attributes are similar, the bit is {@code 0}.
     * When the attributes are dissimilar, the bit is {@code 1}.
     *
     * @param lhs the first item.
     * @param rhs the other item.
     * @return the similarity.
     */
    protected abstract int similar(T lhs, T rhs);
}
