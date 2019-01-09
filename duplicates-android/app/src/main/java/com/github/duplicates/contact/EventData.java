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

import static android.provider.ContactsContract.CommonDataKinds.Event;

/**
 * Contact event data.
 *
 * @author moshe.w
 */
public class EventData extends ContactData {

    public EventData() {
        setMimeType(Event.CONTENT_ITEM_TYPE);
    }

    public String getStartDate() {
        return getData1();
    }

    public int getType() {
        return parseInt(getData2());
    }

    public String getLabel() {
        return getData3();
    }

    @Override
    public String toString() {
        final String label = getLabel();
        return getStartDate() + (TextUtils.isEmpty(label) ? "" : " " + label);
    }

    @Override
    public boolean containsAny(CharSequence s) {
        return contains(getLabel());
    }
}
