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

import android.text.TextUtils;

import static android.provider.ContactsContract.CommonDataKinds.Phone;

/**
 * Contact telephone data.
 *
 * @author moshe.w
 */
public class PhoneData extends ContactData {

    public PhoneData() {
        setMimeType(Phone.CONTENT_ITEM_TYPE);
    }

    public String getNumber() {
        return getData1();
    }

    public int getType() {
        return Integer.parseInt(getData2());
    }

    public String getLabel() {
        return getData3();
    }

    @Override
    public String toString() {
        return getNumber() + (TextUtils.isEmpty(getLabel()) ? "" : " " + getLabel());
    }
}
