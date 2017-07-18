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
package com.github.duplicates.bookmark;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

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
    private Uri url;
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

    public Uri getUrl() {
        return url;
    }

    public void setUrl(Uri url) {
        this.url = url;
    }

    public void setUrl(String url) {
        setUrl(url != null ? Uri.parse(url) : null);
    }

    public int getVisits() {
        return visits;
    }

    public void setVisits(int visits) {
        this.visits = visits;
    }
}
