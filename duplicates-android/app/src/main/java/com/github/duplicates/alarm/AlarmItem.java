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
package com.github.duplicates.alarm;

import android.text.TextUtils;

import com.github.duplicates.DaysOfWeek;
import com.github.duplicates.DuplicateItem;

import androidx.annotation.NonNull;

/**
 * Duplicate alarm item.
 *
 * @author moshe.w
 */
public class AlarmItem extends DuplicateItem {

    private int activate;
    private int alarmTime;
    private long alertTime;
    private long createTime;
    private boolean dailyBriefing;
    private String name;
    private int notificationType;
    private DaysOfWeek repeat;
    private boolean snoozeActivate;
    private int snoozeDoneCount;
    private int snoozeDuration;
    private int snoozeRepeat;
    private int soundTone;
    private int soundType;
    private String soundUri;
    private boolean subdueActivate;
    private int subdueDuration;
    private int subdueTone;
    private int subdueUri;
    private int volume;

    public int getActivate() {
        return activate;
    }

    public void setActivate(int activate) {
        this.activate = activate;
    }

    public int getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(int alarmTime) {
        this.alarmTime = alarmTime;
    }

    public long getAlertTime() {
        return alertTime;
    }

    public void setAlertTime(long alertTime) {
        this.alertTime = alertTime;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public boolean isDailyBriefing() {
        return dailyBriefing;
    }

    public void setDailyBriefing(boolean dailyBriefing) {
        this.dailyBriefing = dailyBriefing;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(int notificationType) {
        this.notificationType = notificationType;
    }

    @NonNull
    public DaysOfWeek getRepeat() {
        if (repeat == null) {
            repeat = new DaysOfWeek(0);
        }
        return repeat;
    }

    public void setRepeat(DaysOfWeek repeat) {
        this.repeat = repeat;
    }

    public boolean isSnoozeActivate() {
        return snoozeActivate;
    }

    public void setSnoozeActivate(boolean snoozeActivate) {
        this.snoozeActivate = snoozeActivate;
    }

    public int getSnoozeDoneCount() {
        return snoozeDoneCount;
    }

    public void setSnoozeDoneCount(int snoozeDoneCount) {
        this.snoozeDoneCount = snoozeDoneCount;
    }

    public int getSnoozeDuration() {
        return snoozeDuration;
    }

    public void setSnoozeDuration(int snoozeDuration) {
        this.snoozeDuration = snoozeDuration;
    }

    public int getSnoozeRepeat() {
        return snoozeRepeat;
    }

    public void setSnoozeRepeat(int snoozeRepeat) {
        this.snoozeRepeat = snoozeRepeat;
    }

    public int getSoundTone() {
        return soundTone;
    }

    public void setSoundTone(int soundTone) {
        this.soundTone = soundTone;
    }

    public int getSoundType() {
        return soundType;
    }

    public void setSoundType(int soundType) {
        this.soundType = soundType;
    }

    public String getSoundUri() {
        return soundUri;
    }

    public void setSoundUri(String soundUri) {
        this.soundUri = soundUri;
    }

    public boolean isSubdueActivate() {
        return subdueActivate;
    }

    public void setSubdueActivate(boolean subdueActivate) {
        this.subdueActivate = subdueActivate;
    }

    public int getSubdueDuration() {
        return subdueDuration;
    }

    public void setSubdueDuration(int subdueDuration) {
        this.subdueDuration = subdueDuration;
    }

    public int getSubdueTone() {
        return subdueTone;
    }

    public void setSubdueTone(int subdueTone) {
        this.subdueTone = subdueTone;
    }

    public int getSubdueUri() {
        return subdueUri;
    }

    public void setSubdueUri(int subdueUri) {
        this.subdueUri = subdueUri;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    @Override
    public boolean contains(CharSequence s) {
        return !TextUtils.isEmpty(name) && name.contains(s);
    }
}
