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
package com.github.duplicates.bookmark;

import com.github.duplicates.DuplicateComparator;

/**
 * Compare duplicate bookmarks.
 *
 * @author moshe.w
 */
public class BookmarkComparator extends DuplicateComparator<BookmarkItem> {

    @Override
    public int compare(BookmarkItem lhs, BookmarkItem rhs) {
        int c;

        c = compare(lhs.getCreated(), rhs.getCreated());
        if (c != SAME) {
            return c;
        }
        c = compare(lhs.getDate(), rhs.getDate());
        if (c != SAME) {
            return c;
        }
        c = compare(lhs.getFavIcon(), rhs.getFavIcon());
        if (c != SAME) {
            return c;
        }
        c = compare(lhs.getTitle(), rhs.getTitle());
        if (c != SAME) {
            return c;
        }
        c = compare(lhs.getUrl(), rhs.getUrl());
        if (c != SAME) {
            return c;
        }
        c = compare(lhs.getVisits(), rhs.getVisits());
        if (c != SAME) {
            return c;
        }

        return super.compare(lhs, rhs);
    }

    @Override
    public float match(BookmarkItem lhs, BookmarkItem rhs) {
        float match = 1f;

        if (compare(lhs.getUrl(), rhs.getUrl()) != SAME) {
            match *= 0.8f;
        }

        if (compare(lhs.getTitle(), rhs.getTitle()) != SAME) {
            match *= 0.9f;
        }

        if (lhs.getCreated() != rhs.getCreated()) {
            match *= 0.95f;
        }
        if (compare(lhs.getFavIcon(), rhs.getFavIcon()) != SAME) {
            match *= 0.95f;
        }

        if (lhs.getDate() != rhs.getDate()) {
            match *= 0.975f;
        }
        if (lhs.getVisits() != rhs.getVisits()) {
            match *= 0.975f;
        }

        return match;
    }
}
