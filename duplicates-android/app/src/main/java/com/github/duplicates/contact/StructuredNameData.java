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
