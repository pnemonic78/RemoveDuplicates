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

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import com.github.duplicates.DuplicateProvider;

import java.util.HashMap;
import java.util.Map;

import static android.provider.BaseColumns._ID;
import static android.provider.ContactsContract.CommonDataKinds.Email;
import static android.provider.ContactsContract.CommonDataKinds.Event;
import static android.provider.ContactsContract.CommonDataKinds.Im;
import static android.provider.ContactsContract.CommonDataKinds.Phone;
import static android.provider.ContactsContract.CommonDataKinds.Photo;
import static android.provider.ContactsContract.CommonDataKinds.StructuredName;
import static android.provider.ContactsContract.Data.CONTACT_ID;
import static android.provider.ContactsContract.Data.DATA1;
import static android.provider.ContactsContract.Data.DATA10;
import static android.provider.ContactsContract.Data.DATA11;
import static android.provider.ContactsContract.Data.DATA12;
import static android.provider.ContactsContract.Data.DATA13;
import static android.provider.ContactsContract.Data.DATA14;
import static android.provider.ContactsContract.Data.DATA15;
import static android.provider.ContactsContract.Data.DATA2;
import static android.provider.ContactsContract.Data.DATA3;
import static android.provider.ContactsContract.Data.DATA4;
import static android.provider.ContactsContract.Data.DATA5;
import static android.provider.ContactsContract.Data.DATA6;
import static android.provider.ContactsContract.Data.DATA7;
import static android.provider.ContactsContract.Data.DATA8;
import static android.provider.ContactsContract.Data.DATA9;
import static android.provider.ContactsContract.Data.DATA_VERSION;
import static android.provider.ContactsContract.Data.DISPLAY_NAME;
import static android.provider.ContactsContract.Data.LOOKUP_KEY;
import static android.provider.ContactsContract.Data.MIMETYPE;
import static android.provider.ContactsContract.Data.PHOTO_THUMBNAIL_URI;
import static android.provider.ContactsContract.Data.RAW_CONTACT_ID;

/**
 * Provide duplicate contacts.
 *
 * @author moshe.w
 */
public class ContactProvider extends DuplicateProvider<ContactItem> {

    private static String[] PERMISSIONS_READ = {Manifest.permission.READ_CONTACTS};
    private static String[] PERMISSIONS_WRITE = {Manifest.permission.WRITE_CONTACTS};

    private static final String[] PROJECTION = {
            _ID,
            CONTACT_ID,
            RAW_CONTACT_ID,
            LOOKUP_KEY,
            DISPLAY_NAME,
            PHOTO_THUMBNAIL_URI,
            DATA1,
            DATA2,
            DATA3,
            DATA4,
            DATA5,
            DATA6,
            DATA7,
            DATA8,
            DATA9,
            DATA10,
            DATA11,
            DATA12,
            DATA13,
            DATA14,
            DATA15,
            DATA_VERSION,
            MIMETYPE
    };

    private static final int INDEX_ID = 0;
    private static final int INDEX_CONTACT_ID = 1;
    private static final int INDEX_RAW_CONTACT_ID = 2;
    private static final int INDEX_LOOKUP_KEY = 3;
    private static final int INDEX_DISPLAY_NAME = 4;
    private static final int INDEX_PHOTO_THUMBNAIL_URI = 5;
    private static final int INDEX_DATA1 = 6;
    private static final int INDEX_DATA2 = 7;
    private static final int INDEX_DATA3 = 8;
    private static final int INDEX_DATA4 = 9;
    private static final int INDEX_DATA5 = 10;
    private static final int INDEX_DATA6 = 11;
    private static final int INDEX_DATA7 = 12;
    private static final int INDEX_DATA8 = 13;
    private static final int INDEX_DATA9 = 14;
    private static final int INDEX_DATA10 = 15;
    private static final int INDEX_DATA11 = 16;
    private static final int INDEX_DATA12 = 17;
    private static final int INDEX_DATA13 = 18;
    private static final int INDEX_DATA14 = 19;
    private static final int INDEX_DATA15 = 20;
    private static final int INDEX_DATA_VERSION = 21;
    private static final int INDEX_MIME_TYPE = 22;

    private final Map<Long, ContactItem> contacts = new HashMap<>();

    public ContactProvider(Context context) {
        super(context);
    }

    @Override
    protected Uri getContentUri() {
        return ContactsContract.Data.CONTENT_URI;
    }

    @Override
    protected String[] getCursorProjection() {
        return PROJECTION;
    }

    @Override
    public ContactItem createItem(Cursor cursor) {
        Long id = cursor.getLong(INDEX_CONTACT_ID);
        ContactItem item = contacts.get(id);
        if (item != null) {
            return item;
        }
        item = new ContactItem();
        contacts.put(id, item);
        return item;
    }

    @Override
    public void onPreExecute() {
        super.onPreExecute();
        contacts.clear();
    }

    @Override
    public void populateItem(Cursor cursor, ContactItem item) {
        item.setId(cursor.getLong(INDEX_CONTACT_ID));
        item.setLookup(cursor.getString(INDEX_LOOKUP_KEY));
        item.setDisplayName(cursor.getString(INDEX_DISPLAY_NAME));
        item.setPhotoThumbnailUri(cursor.getString(INDEX_PHOTO_THUMBNAIL_URI));

        ContactData data;
        String mimeType = cursor.getString(INDEX_MIME_TYPE);
        switch (mimeType) {
            case Email.CONTENT_ITEM_TYPE:
                data = new EmailData();
                populateItemData(cursor, data);
                item.getEmails().add((EmailData) data);
                break;
            case Event.CONTENT_ITEM_TYPE:
                data = new EventData();
                populateItemData(cursor, data);
                item.getEvents().add((EventData) data);
                break;
            case Im.CONTENT_ITEM_TYPE:
                data = new ImData();
                populateItemData(cursor, data);
                item.getIms().add((ImData) data);
                break;
            case Phone.CONTENT_ITEM_TYPE:
                data = new PhoneData();
                populateItemData(cursor, data);
                item.getPhones().add((PhoneData) data);
                break;
            case Photo.CONTENT_ITEM_TYPE:
                data = new PhotoData();
                populateItemData(cursor, data);
                item.getPhotos().add((PhotoData) data);
                break;
            case StructuredName.CONTENT_ITEM_TYPE:
                data = new StructuredNameData();
                populateItemData(cursor, data);
                item.getNames().add((StructuredNameData) data);
                break;
        }
    }

    @Override
    public boolean deleteItem(ContentResolver cr, ContactItem item) {
        return cr.delete(getContentUri(), CONTACT_ID + "=" + item.getId(), null) > 0;
    }

    @Override
    public String[] getReadPermissions() {
        return PERMISSIONS_READ;
    }

    @Override
    public String[] getDeletePermissions() {
        return PERMISSIONS_WRITE;
    }


    protected void populateItemData(Cursor cursor, ContactData data) {
        data.setId(cursor.getLong(INDEX_ID));
        data.setContactId(cursor.getLong(INDEX_CONTACT_ID));
        data.setRawContactId(cursor.getLong(INDEX_RAW_CONTACT_ID));
        data.setData1(cursor.getString(INDEX_DATA1));
        data.setData2(cursor.getString(INDEX_DATA2));
        data.setData3(cursor.getString(INDEX_DATA3));
        data.setData4(cursor.getString(INDEX_DATA4));
        data.setData5(cursor.getString(INDEX_DATA5));
        data.setData6(cursor.getString(INDEX_DATA6));
        data.setData7(cursor.getString(INDEX_DATA7));
        data.setData8(cursor.getString(INDEX_DATA8));
        data.setData9(cursor.getString(INDEX_DATA9));
        data.setData10(cursor.getString(INDEX_DATA10));
        data.setData11(cursor.getString(INDEX_DATA11));
        data.setData12(cursor.getString(INDEX_DATA12));
        data.setData13(cursor.getString(INDEX_DATA13));
        data.setData14(cursor.getString(INDEX_DATA14));
        data.setData15(cursor.getString(INDEX_DATA15));
        data.setDataVersion(cursor.getInt(INDEX_DATA_VERSION));
        if (data.getMimeType() == null) {
            data.setMimeType(cursor.getString(INDEX_MIME_TYPE));
        }
    }
}
