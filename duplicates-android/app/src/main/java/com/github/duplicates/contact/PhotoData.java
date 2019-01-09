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

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Base64;

import static android.provider.ContactsContract.CommonDataKinds.Photo;

/**
 * Contact photograph data.
 *
 * @author moshe.w
 */
public class PhotoData extends ContactData {

    private Bitmap photo;

    public PhotoData() {
        setMimeType(Photo.CONTENT_ITEM_TYPE);
    }

    public long getPhotoFileId() {
        return Long.parseLong(getData14());
    }

    @Override
    public void setData15(String data15) {
        super.setData15(data15);
        this.photo = null;
    }

    public Bitmap getPhoto() {
        if (photo == null) {
            final String data15 = getData15();
            if (!TextUtils.isEmpty(data15)) {
                byte[] blob = Base64.decode(data15, Base64.DEFAULT);
                if (blob != null) {
                    this.photo = BitmapFactory.decodeByteArray(blob, 0, blob.length);
                }
            }
        }
        return photo;
    }

    @Override
    public String toString() {
        return getData15();
    }

    @Override
    public boolean isEmpty() {
        return getData15() == null;
    }

    @Override
    public boolean containsAny(CharSequence s) {
        return false;
    }
}
