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

import static android.provider.ContactsContract.CommonDataKinds.StructuredName;

/**
 * Contact structured name data.
 *
 * @author moshe.w
 */
public class StructuredNameData extends ContactData {

    public StructuredNameData() {
        setMimeType(StructuredName.CONTENT_ITEM_TYPE);
    }

    public String getDisplayName() {
        return getData1();
    }

    public String getGivenName() {
        return getData2();
    }

    public String getFamilyName() {
        return getData3();
    }

    public String getPrefix() {
        return getData4();
    }

    public String getMiddleName() {
        return getData5();
    }

    public String getSuffix() {
        return getData6();
    }

    public String getPhoneticGivenName() {
        return getData7();
    }

    public String getPhoneticMiddleName() {
        return getData8();
    }

    public String getPhoneticFamilyName() {
        return getData9();
    }

    @Override
    public String toString() {
        return getDisplayName();
    }
}
