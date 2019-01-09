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
package com.github.duplicates;

import static com.github.duplicates.DuplicateComparator.compare;

/**
 * Item that is a duplicate of some other item.
 *
 * @author moshe.w
 */
public abstract class DuplicateItem implements Comparable<DuplicateItem> {

    private long id;
    private boolean checked;
    private boolean error;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public int compareTo(DuplicateItem another) {
        return compare(this.getId(), another.getId());
    }

    @Override
    public int hashCode() {
        return (int) getId();
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public boolean isError() {
        return error;
    }

    public abstract boolean contains(CharSequence s);
}
