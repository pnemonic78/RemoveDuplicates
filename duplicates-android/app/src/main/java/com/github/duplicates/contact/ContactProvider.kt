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
package com.github.duplicates.contact

import android.Manifest
import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.database.Cursor.FIELD_TYPE_BLOB
import android.database.Cursor.FIELD_TYPE_FLOAT
import android.database.Cursor.FIELD_TYPE_INTEGER
import android.database.Cursor.FIELD_TYPE_NULL
import android.net.Uri
import android.provider.BaseColumns._ID
import android.provider.ContactsContract.CommonDataKinds.Email
import android.provider.ContactsContract.CommonDataKinds.Event
import android.provider.ContactsContract.CommonDataKinds.Im
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.provider.ContactsContract.CommonDataKinds.Photo
import android.provider.ContactsContract.CommonDataKinds.StructuredName
import android.provider.ContactsContract.Data.CONTACT_ID
import android.provider.ContactsContract.Data.CONTENT_URI
import android.provider.ContactsContract.Data.DATA1
import android.provider.ContactsContract.Data.DATA10
import android.provider.ContactsContract.Data.DATA11
import android.provider.ContactsContract.Data.DATA12
import android.provider.ContactsContract.Data.DATA13
import android.provider.ContactsContract.Data.DATA14
import android.provider.ContactsContract.Data.DATA15
import android.provider.ContactsContract.Data.DATA2
import android.provider.ContactsContract.Data.DATA3
import android.provider.ContactsContract.Data.DATA4
import android.provider.ContactsContract.Data.DATA5
import android.provider.ContactsContract.Data.DATA6
import android.provider.ContactsContract.Data.DATA7
import android.provider.ContactsContract.Data.DATA8
import android.provider.ContactsContract.Data.DATA9
import android.provider.ContactsContract.Data.DATA_VERSION
import android.provider.ContactsContract.Data.DISPLAY_NAME
import android.provider.ContactsContract.Data.LOOKUP_KEY
import android.provider.ContactsContract.Data.MIMETYPE
import android.provider.ContactsContract.Data.PHOTO_THUMBNAIL_URI
import android.provider.ContactsContract.Data.RAW_CONTACT_ID
import android.provider.ContactsContract.RawContacts
import android.provider.ContactsContract.RawContacts.ACCOUNT_NAME
import android.provider.ContactsContract.RawContacts.ACCOUNT_TYPE
import android.util.Base64
import android.util.LongSparseArray
import com.github.duplicates.DuplicateItemPair
import com.github.duplicates.DuplicateProvider
import com.github.duplicates.DuplicateProviderListener
import java.util.*
import java.util.concurrent.*

/**
 * Provide duplicate contacts.
 *
 * @author moshe.w
 */
class ContactProvider(context: Context) : DuplicateProvider<ContactItem>(context) {

    private val contacts = LongSparseArray<ContactItem>()

    override fun getContentUri(): Uri {
        return CONTENT_URI
    }

    override fun getCursorProjection(): Array<String> {
        return PROJECTION
    }

    override fun createItem(cursor: Cursor): ContactItem {
        val id = cursor.getLong(INDEX_CONTACT_ID)
        var item: ContactItem? = contacts.get(id)
        if (item != null) {
            return item
        }
        item = ContactItem()
        contacts.put(id, item)
        return item
    }

    override fun onPreExecute() {
        super.onPreExecute()
        contacts.clear()
    }

    override fun populateItem(cursor: Cursor, item: ContactItem) {
        item.id = cursor.getLong(INDEX_CONTACT_ID)
        item.accountName = cursor.getString(INDEX_ACCOUNT_NAME)
        item.accountType = cursor.getString(INDEX_ACCOUNT_TYPE)
        item.lookup = cursor.getString(INDEX_LOOKUP_KEY)
        item.displayName = cursor.getString(INDEX_DISPLAY_NAME)
        item.setPhotoThumbnailUri(cursor.getString(INDEX_PHOTO_THUMBNAIL_URI))

        val data: ContactData
        val mimeType = cursor.getString(INDEX_MIME_TYPE)
        when (mimeType) {
            Email.CONTENT_ITEM_TYPE -> {
                data = EmailData()
                populateItemData(cursor, data)
                if (!data.isEmpty) {
                    item.emails.add(data)
                }
            }
            Event.CONTENT_ITEM_TYPE -> {
                data = EventData()
                populateItemData(cursor, data)
                if (!data.isEmpty) {
                    item.events.add(data)
                }
            }
            Im.CONTENT_ITEM_TYPE -> {
                data = ImData()
                populateItemData(cursor, data)
                if (!data.isEmpty) {
                    item.ims.add(data)
                }
            }
            Phone.CONTENT_ITEM_TYPE -> {
                data = PhoneData()
                populateItemData(cursor, data)
                if (!data.isEmpty) {
                    item.phones.add(data)
                }
            }
            Photo.CONTENT_ITEM_TYPE -> {
                data = PhotoData()
                populateItemData(cursor, data)
                if (!data.isEmpty) {
                    item.photos.add(data)
                }
            }
            StructuredName.CONTENT_ITEM_TYPE -> {
                data = StructuredNameData()
                populateItemData(cursor, data)
                if (!data.isEmpty) {
                    item.names.add(data)
                }
            }
        }
    }

    @Throws(CancellationException::class)
    override fun fetchItems(listener: DuplicateProviderListener<ContactItem, DuplicateProvider<ContactItem>>) {
        val items = ArrayList<ContactItem>()
        val listener2 =
            object : DuplicateProviderListener<ContactItem, DuplicateProvider<ContactItem>> {
                override fun onItemFetched(
                    provider: DuplicateProvider<ContactItem>,
                    count: Int,
                    item: ContactItem
                ) {
                    val size = items.size

                    // Maybe the item already exists in the list?
                    var item1: ContactItem
                    for (i in size - 1 downTo 0) {
                        item1 = items[i]
                        if (item === item1) {
                            return
                        }
                    }

                    items.add(item)
                }

                override fun onItemDeleted(
                    provider: DuplicateProvider<ContactItem>,
                    count: Int,
                    item: ContactItem
                ) {
                }

                override fun onPairDeleted(
                    provider: DuplicateProvider<ContactItem>,
                    count: Int,
                    pair: DuplicateItemPair<ContactItem>
                ) {
                }
            }
        super.fetchItems(listener2)

        if (isCancelled) {
            return
        }

        // Now that we have all the items, we can match them.
        items.sortWith(ContactNameComparator())
        val size = items.size
        for (i in 0 until size) {
            if (isCancelled) {
                return
            }
            listener.onItemFetched(this, i, items[i])
        }
    }

    override fun deleteItem(cr: ContentResolver, contentUri: Uri, item: ContactItem): Boolean {
        return cr.delete(contentUri, RawContacts.CONTACT_ID + "=" + item.id, null) > 0
    }

    override fun getReadPermissions(): Array<String> {
        return PERMISSIONS_READ
    }

    override fun getDeletePermissions(): Array<String> {
        return PERMISSIONS_WRITE
    }


    protected fun populateItemData(cursor: Cursor, data: ContactData) {
        data.id = cursor.getLong(INDEX_ID)
        data.contactId = cursor.getLong(INDEX_CONTACT_ID)
        data.rawContactId = cursor.getLong(INDEX_RAW_CONTACT_ID)
        data.data1 = toString(cursor, INDEX_DATA1)
        data.data2 = toString(cursor, INDEX_DATA2)
        data.data3 = toString(cursor, INDEX_DATA3)
        data.data4 = toString(cursor, INDEX_DATA4)
        data.data5 = toString(cursor, INDEX_DATA5)
        data.data6 = toString(cursor, INDEX_DATA6)
        data.data7 = toString(cursor, INDEX_DATA7)
        data.data8 = toString(cursor, INDEX_DATA8)
        data.data9 = toString(cursor, INDEX_DATA9)
        data.data10 = toString(cursor, INDEX_DATA10)
        data.data11 = toString(cursor, INDEX_DATA11)
        data.data12 = toString(cursor, INDEX_DATA12)
        data.data13 = toString(cursor, INDEX_DATA13)
        data.data14 = toString(cursor, INDEX_DATA14)
        data.data15 = toString(cursor, INDEX_DATA15)
        data.dataVersion = cursor.getInt(INDEX_DATA_VERSION)
        if (data.mimeType == null) {
            data.mimeType = cursor.getString(INDEX_MIME_TYPE)
        }
    }

    protected fun toString(cursor: Cursor, index: Int): String? {
        when (cursor.getType(index)) {
            FIELD_TYPE_NULL -> return null
            FIELD_TYPE_BLOB -> {
                val blob = cursor.getBlob(index)
                return Base64.encodeToString(blob, Base64.DEFAULT)
            }
            FIELD_TYPE_FLOAT -> return cursor.getFloat(index).toString()
            FIELD_TYPE_INTEGER -> return cursor.getInt(index).toString()
            else -> return cursor.getString(index)
        }
    }

    companion object {

        private val PERMISSIONS_READ = arrayOf(Manifest.permission.READ_CONTACTS)
        private val PERMISSIONS_WRITE = arrayOf(Manifest.permission.WRITE_CONTACTS)

        private val PROJECTION = arrayOf(
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
            MIMETYPE,
            ACCOUNT_NAME,
            ACCOUNT_TYPE
        )

        private const val INDEX_ID = 0
        private const val INDEX_CONTACT_ID = 1
        private const val INDEX_RAW_CONTACT_ID = 2
        private const val INDEX_LOOKUP_KEY = 3
        private const val INDEX_DISPLAY_NAME = 4
        private const val INDEX_PHOTO_THUMBNAIL_URI = 5
        private const val INDEX_DATA1 = 6
        private const val INDEX_DATA2 = 7
        private const val INDEX_DATA3 = 8
        private const val INDEX_DATA4 = 9
        private const val INDEX_DATA5 = 10
        private const val INDEX_DATA6 = 11
        private const val INDEX_DATA7 = 12
        private const val INDEX_DATA8 = 13
        private const val INDEX_DATA9 = 14
        private const val INDEX_DATA10 = 15
        private const val INDEX_DATA11 = 16
        private const val INDEX_DATA12 = 17
        private const val INDEX_DATA13 = 18
        private const val INDEX_DATA14 = 19
        private const val INDEX_DATA15 = 20
        private const val INDEX_DATA_VERSION = 21
        private const val INDEX_MIME_TYPE = 22
        private const val INDEX_ACCOUNT_NAME = 23
        private const val INDEX_ACCOUNT_TYPE = 24
    }
}
