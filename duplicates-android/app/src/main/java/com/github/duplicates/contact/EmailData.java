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

import static android.provider.ContactsContract.CommonDataKinds.Email;

/**
 * Contact email data.
 *
 * @author moshe.w
 */
public class EmailData extends ContactData {

    public EmailData() {
        setMimeType(Email.CONTENT_ITEM_TYPE);
    }

    @Override
    public void setData1(String data1) {
        super.setData1(data1 != null ? data1.toLowerCase() : data1);
    }

    public String getAddress() {
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
        return getAddress();
    }
}
