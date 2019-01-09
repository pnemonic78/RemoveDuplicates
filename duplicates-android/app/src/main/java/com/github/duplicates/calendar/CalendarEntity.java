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
package com.github.duplicates.calendar;

import android.graphics.Color;
import android.provider.CalendarContract;
import android.text.TextUtils;

import java.util.TimeZone;

/**
 * Calendar entity.
 *
 * @author moshe.w
 */
public class CalendarEntity {

    private long id;
    private String name;
    private int color = Color.TRANSPARENT;
    private TimeZone timeZone;
    private int access = CalendarContract.CalendarEntity.CAL_ACCESS_NONE;
    private String account;
    private boolean visible = true;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }

    public void setTimeZone(String timeZone) {
        setTimeZone(timeZone != null ? TimeZone.getTimeZone(timeZone) : null);
    }

    public int getAccess() {
        return access;
    }

    public void setAccess(int access) {
        this.access = access;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public String getDisplayName() {
        return TextUtils.isEmpty(getName()) ? getAccount() : getName();
    }
}
