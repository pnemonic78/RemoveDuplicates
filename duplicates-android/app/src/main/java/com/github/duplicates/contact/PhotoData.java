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

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
            String data15 = getData15();
            if (data15 != null) {
                byte[] blob = Base64.decode(data15, Base64.DEFAULT);
                if (blob != null) {
                    this.photo = BitmapFactory.decodeByteArray(blob, 0, blob.length);
                }
            }
        }
        return photo;
    }
}
