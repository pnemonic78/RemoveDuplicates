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

import android.net.Uri;
import android.support.annotation.NonNull;

import com.github.duplicates.message.MessageItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Duplicate contact item.
 *
 * @author moshe.w
 */
public class ContactItem extends MessageItem {

    private String lookup;
    private String displayName;
    private Uri photoThumbnailUri;
    private List<EmailData> emails;
    private List<EventData> events;
    private List<ImData> ims;
    private List<PhoneData> phones;
    private List<PhotoData> photos;
    private List<StructuredNameData> names;

    public String getLookup() {
        return lookup;
    }

    public void setLookup(String lookup) {
        this.lookup = lookup;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Uri getPhotoThumbnailUri() {
        return photoThumbnailUri;
    }

    public void setPhotoThumbnailUri(Uri photoThumbnailUri) {
        this.photoThumbnailUri = photoThumbnailUri;
    }

    public void setPhotoThumbnailUri(String photoThumbnailPath) {
        setPhotoThumbnailUri(photoThumbnailPath != null ? Uri.parse(photoThumbnailPath) : null);
    }

    @NonNull
    public List<EmailData> getEmails() {
        if (emails == null) {
            emails = new ArrayList<>();
        }
        return emails;
    }

    public void setEmails(List<EmailData> emails) {
        this.emails = emails;
    }

    @NonNull
    public List<EventData> getEvents() {
        if (events == null) {
            events = new ArrayList<>();
        }
        return events;
    }

    public void setEvents(List<EventData> events) {
        this.events = events;
    }

    @NonNull
    public List<ImData> getIms() {
        if (ims == null) {
            ims = new ArrayList<>();
        }
        return ims;
    }

    public void setIms(List<ImData> ims) {
        this.ims = ims;
    }

    @NonNull
    public List<PhoneData> getPhones() {
        if (phones == null) {
            phones = new ArrayList<>();
        }
        return phones;
    }

    public void setPhones(List<PhoneData> phones) {
        this.phones = phones;
    }

    @NonNull
    public List<PhotoData> getPhotos() {
        if (photos == null) {
            photos = new ArrayList<>();
        }
        return photos;
    }

    public void setPhotos(List<PhotoData> photos) {
        this.photos = photos;
    }

    @NonNull
    public List<StructuredNameData> getNames() {
        if (names == null) {
            names = new ArrayList<>();
        }
        return names;
    }

    public void setNames(List<StructuredNameData> names) {
        this.names = names;
    }

    @Override
    public String toString() {
        return Long.toString(getId()) + ": " + getDisplayName();
    }
}
