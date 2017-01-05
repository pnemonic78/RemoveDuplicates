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
package com.github.duplicates.bookmark;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.github.duplicates.DuplicateItem;

import java.io.ByteArrayOutputStream;

/**
 * Duplicate bookmark item.
 *
 * @author moshe.w
 */
public class BookmarkItem extends DuplicateItem {

    private long created;
    private long date;
    private byte[] favIcon;
    private Bitmap icon;
    private String title;
    private String url;
    private int visits;

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public byte[] getFavIcon() {
        return favIcon;
    }

    public void setFavIcon(byte[] favIcon) {
        this.favIcon = favIcon;
        this.icon = null;
    }

    public Bitmap getIcon() {
        if ((icon == null) && (favIcon != null)) {
            icon = BitmapFactory.decodeByteArray(favIcon, 0, favIcon.length);
        }
        return icon;
    }

    public void setIcon(Bitmap icon) {
        this.favIcon = null;
        this.icon = icon;
        if (icon != null) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            icon.compress(Bitmap.CompressFormat.PNG, 100, out);
            this.favIcon = out.toByteArray();
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getVisits() {
        return visits;
    }

    public void setVisits(int visits) {
        this.visits = visits;
    }
}
