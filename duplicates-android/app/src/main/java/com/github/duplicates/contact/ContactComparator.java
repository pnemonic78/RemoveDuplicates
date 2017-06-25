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

import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;

import com.github.duplicates.DuplicateComparator;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    private static final float MATCH_DATA = 0.85f;
    private static final float MATCH_NAME = 0.8f;

    @Override
    public int compare(ContactItem lhs, ContactItem rhs) {
        int c;

        c = compareData(lhs.getEmails(), rhs.getEmails());
        if (c != SAME) {
            return c;
        }
        c = compareData(lhs.getEvents(), rhs.getEvents());
        if (c != SAME) {
            return c;
        }
        c = compareData(lhs.getIms(), rhs.getIms());
        if (c != SAME) {
            return c;
        }
        c = compareData(lhs.getNames(), rhs.getNames());
        if (c != SAME) {
            return c;
        }
        c = compareData(lhs.getPhones(), rhs.getPhones());
        if (c != SAME) {
            return c;
        }

        return super.compare(lhs, rhs);
    }

    @Override
    public boolean[] difference(ContactItem lhs, ContactItem rhs) {
        boolean[] result = new boolean[5];

        result[EMAIL] = compareData(lhs.getEmails(), rhs.getEmails()) != SAME;
        result[EVENT] = compareData(lhs.getEvents(), rhs.getEvents()) != SAME;
        result[IM] = compareData(lhs.getIms(), rhs.getIms()) != SAME;
        result[NAME] = compareData(lhs.getNames(), rhs.getNames()) != SAME;
        result[PHONE] = compareData(lhs.getPhones(), rhs.getPhones()) != SAME;

        return result;
    }

    @Override
    public float match(ContactItem lhs, ContactItem rhs, boolean[] difference) {
        if (difference == null) {
            difference = difference(lhs, rhs);
        }
        float match = MATCH_SAME;

        if (difference[EMAIL]) {
            match *= matchEmails(lhs.getEmails(), rhs.getEmails());
        }
        if (difference[EVENT]) {
            match *= matchEvents(lhs.getEvents(), rhs.getEvents());
        }
        if (difference[IM]) {
            match *= matchIms(lhs.getIms(), rhs.getIms());
        }
        if (difference[NAME]) {
            match *= matchNames(lhs.getNames(), rhs.getNames());
        }
        if (difference[PHONE]) {
            match *= matchPhones(lhs.getPhones(), rhs.getPhones());
        }

        return match;
    }

    public static int compareData(Collection<? extends ContactData> lhs, Collection<? extends ContactData> rhs) {
        if (lhs.isEmpty()) {
            return rhs.isEmpty() ? SAME : RHS;
        }
        if (rhs.isEmpty()) {
            return LHS;
        }
        Set<String> set1 = new HashSet<>(lhs.size());
        for (ContactData datum : lhs) {
            set1.add(datum.toString().toLowerCase());
        }
        Set<String> set2 = new HashSet<>(rhs.size());
        for (ContactData datum : rhs) {
            set2.add(datum.toString().toLowerCase());
        }
        return compare(set1, set2);
    }

    protected float matchEmails(List<EmailData> lhs, List<EmailData> rhs) {
        if (lhs.isEmpty()) {
            return rhs.isEmpty() ? MATCH_SAME : MATCH_DATA;
        }
        if (rhs.isEmpty()) {
            return MATCH_DATA;
        }
        String s1;
        for (EmailData d1 : lhs) {
            s1 = d1.getAddress();
            for (EmailData d2 : rhs) {
                if (compareIgnoreCase(s1, d2.getAddress()) == SAME) {
                    return MATCH_SAME;
                }
            }
        }
        return MATCH_DATA;
    }

    protected float matchEvents(List<EventData> lhs, List<EventData> rhs) {
        if (lhs.isEmpty()) {
            return rhs.isEmpty() ? MATCH_SAME : MATCH_DATA;
        }
        if (rhs.isEmpty()) {
            return MATCH_DATA;
        }
        String s1;
        int t1;
        for (EventData d1 : lhs) {
            s1 = d1.getStartDate();
            t1 = d1.getType();
            for (EventData d2 : rhs) {
                if ((compare(s1, d2.getStartDate()) == SAME) && (compare(t1, d2.getType()) == SAME)) {
                    return MATCH_SAME;
                }
            }
        }
        return MATCH_DATA;
    }

    protected float matchIms(List<ImData> lhs, List<ImData> rhs) {
        if (lhs.isEmpty()) {
            return rhs.isEmpty() ? MATCH_SAME : MATCH_DATA;
        }
        if (rhs.isEmpty()) {
            return MATCH_DATA;
        }
        String s1;
        for (ImData d1 : lhs) {
            s1 = d1.getData();
            for (ImData d2 : rhs) {
                if (compareIgnoreCase(s1, d2.getData()) == SAME) {
                    return MATCH_SAME;
                }
            }
        }
        return MATCH_DATA;
    }

    protected float matchNames(List<StructuredNameData> lhs, List<StructuredNameData> rhs) {
        if (lhs.isEmpty()) {
            return rhs.isEmpty() ? MATCH_SAME : MATCH_NAME;
        }
        if (rhs.isEmpty()) {
            return MATCH_NAME;
        }
        String s1, g1, m1, f1;
        String g2, m2, f2;
        boolean g1Empty, f1Empty;
        boolean g2Empty, f2Empty;
        for (StructuredNameData d1 : lhs) {
            s1 = d1.getDisplayName();
            g1 = d1.getGivenName();
            g1Empty = TextUtils.isEmpty(g1);
            m1 = d1.getMiddleName();
            if (g1Empty) {
                g1 = m1;
                g1Empty = TextUtils.isEmpty(g1);
            }
            f1 = d1.getFamilyName();
            f1Empty = TextUtils.isEmpty(f1);
            for (StructuredNameData d2 : rhs) {
                if (compareIgnoreCase(s1, d2.getDisplayName()) == SAME) {
                    return MATCH_SAME;
                }
                g2 = d2.getGivenName();
                g2Empty = TextUtils.isEmpty(g2);
                m2 = d2.getMiddleName();
                if (g2Empty) {
                    g2 = m2;
                    g2Empty = TextUtils.isEmpty(g2);
                }
                f2 = d2.getFamilyName();
                f2Empty = TextUtils.isEmpty(f2);
                // if (g1 = g2 and f1 = f2) or (g1 = g2 and either f1 is empty or f2 is empty) or (f1 = f2 and either g1 is empty or g2 is empty)
                if (compareIgnoreCase(g1, g2) == SAME) {
                    if (compareIgnoreCase(f1, f2) == SAME) {
                        return MATCH_SAME;
                    }
                    if (f1Empty || f2Empty) {
                        return MATCH_SAME;
                    }
                } else if (compareIgnoreCase(f1, f2) == SAME) {
                    if (g1Empty || g2Empty) {
                        return MATCH_SAME;
                    }
                }
            }
        }
        return MATCH_NAME;
    }

    protected float matchPhones(List<PhoneData> lhs, List<PhoneData> rhs) {
        if (lhs.isEmpty()) {
            return rhs.isEmpty() ? MATCH_SAME : MATCH_DATA;
        }
        if (rhs.isEmpty()) {
            return MATCH_DATA;
        }
        String s1;
        for (PhoneData d1 : lhs) {
            s1 = d1.getNumber();
            for (PhoneData d2 : rhs) {
                if (PhoneNumberUtils.compare(s1, d2.getNumber())) {
                    return MATCH_SAME;
                }
            }
        }
        return MATCH_DATA;
    }
}
