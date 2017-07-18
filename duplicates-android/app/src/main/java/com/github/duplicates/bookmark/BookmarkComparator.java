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
package com.github.duplicates.bookmark;

import android.text.format.DateUtils;

import com.github.duplicates.DuplicateComparator;

/**
 * Compare duplicate bookmarks.
 *
 * @author moshe.w
 */
public class BookmarkComparator extends DuplicateComparator<BookmarkItem> {

    public static final int URL = 0;
    public static final int TITLE = 1;
    public static final int CREATED = 2;
    public static final int FAVICON = 3;
    public static final int DATE = 4;
    public static final int VISITS = 5;

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
    public boolean[] difference(BookmarkItem lhs, BookmarkItem rhs) {
        boolean[] result = new boolean[6];

        result[CREATED] = compareTime(lhs.getCreated(), rhs.getCreated(), DateUtils.MINUTE_IN_MILLIS) != SAME;
        result[DATE] = compareTime(lhs.getDate(), rhs.getDate(), DateUtils.MINUTE_IN_MILLIS) != SAME;
        result[FAVICON] = compare(lhs.getFavIcon(), rhs.getFavIcon()) != SAME;
        result[TITLE] = compareIgnoreCase(lhs.getTitle(), rhs.getTitle()) != SAME;
        result[URL] = compare(lhs.getUrl(), rhs.getUrl()) != SAME;
        result[VISITS] = compare(lhs.getVisits(), rhs.getVisits()) != SAME;

        return result;
    }

    @Override
    public float match(BookmarkItem lhs, BookmarkItem rhs, boolean[] difference) {
        if (difference == null) {
            difference = difference(lhs, rhs);
        }
        float match = MATCH_SAME;

        if (difference[URL]) {
            match *= 0.8f;
        }

        if (difference[TITLE]) {
            match *= 0.9f;
        }

        if (difference[CREATED]) {
            match *= 0.95f;
        }
        if (difference[FAVICON]) {
            match *= 0.95f;
        }

        if (difference[DATE]) {
            match *= 0.975f;
        }
        if (difference[VISITS]) {
            match *= 0.975f;
        }

        return match;
    }
}
