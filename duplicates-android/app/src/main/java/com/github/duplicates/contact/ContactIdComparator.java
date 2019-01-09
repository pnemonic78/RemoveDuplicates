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

import com.github.duplicates.DuplicateComparator;

import java.util.Comparator;

/**
 * Compare contacts by ID.
 *
 * @author moshe.w
 */
public class ContactIdComparator implements Comparator<ContactItem> {

    @Override
    public int compare(ContactItem lhs, ContactItem rhs) {
        return DuplicateComparator.compare(lhs.getId(), rhs.getId());
    }
}
