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
package com.github.duplicates.contact;

import com.github.duplicates.DuplicateComparator;

import java.util.Collection;

/**
 * Compare duplicate contacts.
 *
 * @author moshe.w
 */
public class ContactComparator extends DuplicateComparator<ContactItem> {

    public static final int EMAIL = 0;
    public static final int EVENT = 1;
    public static final int IM = 2;
    public static final int NAME = 3;
    public static final int PHONE = 4;

    @Override
    public int compare(ContactItem lhs, ContactItem rhs) {
        int c;

        c = compare(lhs.getEmails(), rhs.getEmails());
        if (c != SAME) {
            return c;
        }
        c = compare(lhs.getEvents(), rhs.getEvents());
        if (c != SAME) {
            return c;
        }
        c = compare(lhs.getIms(), rhs.getIms());
        if (c != SAME) {
            return c;
        }
        c = compare(lhs.getPhones(), rhs.getPhones());
        if (c != SAME) {
            return c;
        }
        c = compare(lhs.getNames(), rhs.getNames());
        if (c != SAME) {
            return c;
        }

        return super.compare(lhs, rhs);
    }

    @Override
    public boolean[] difference(ContactItem lhs, ContactItem rhs) {
        boolean[] result = new boolean[5];

        result[EMAIL] = compare(lhs.getEmails(), rhs.getEmails()) != SAME;
        result[EVENT] = compare(lhs.getEvents(), rhs.getEvents()) != SAME;
        result[IM] = compare(lhs.getIms(), rhs.getIms()) != SAME;
        result[NAME] = compare(lhs.getNames(), rhs.getNames()) != SAME;
        result[PHONE] = compare(lhs.getPhones(), rhs.getPhones()) != SAME;

        return result;
    }

    @Override
    public float match(boolean[] difference) {
        float match = 1f;

        if (difference[EMAIL]) {
            match *= 0.85f;
        }
        if (difference[EVENT]) {
            match *= 0.85f;
        }
        if (difference[IM]) {
            match *= 0.85f;
        }
        if (difference[NAME]) {
            match *= 0.85f;
        }
        if (difference[PHONE]) {
            match *= 0.85f;
        }

        return match;
    }

    public static int compare(Collection<? extends ContactData> lhs, Collection<? extends ContactData> rhs) {
        return lhs.size() - rhs.size();
    }
}
