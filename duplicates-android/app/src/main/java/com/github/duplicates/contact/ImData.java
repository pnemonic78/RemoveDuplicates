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

import android.text.TextUtils;

import static android.provider.ContactsContract.CommonDataKinds.Im;

/**
 * Contact instant-messaging data.
 *
 * @author moshe.w
 */
public class ImData extends ContactData {

    public ImData() {
        setMimeType(Im.CONTENT_ITEM_TYPE);
    }

    public String getData() {
        return getData1();
    }

    public int getType() {
        return Integer.parseInt(getData2());
    }

    public String getLabel() {
        return getData3();
    }

    public int getProtocol() {
        return Integer.parseInt(getData5());
    }

    public String getCustomProtocol() {
        return getData6();
    }

    @Override
    public String toString() {
        return getData() + (TextUtils.isEmpty(getLabel()) ? "" : " " + getLabel());
    }

    @Override
    public boolean contains(CharSequence s) {
        return !TextUtils.isEmpty(getLabel()) && getLabel().contains(s);
    }
}
