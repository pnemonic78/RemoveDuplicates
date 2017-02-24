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
package com.github.duplicates.call;

import com.github.duplicates.DuplicateItem;

/**
 * Duplicate call log entry.
 *
 * @author moshe.w
 */
public class CallLogItem extends DuplicateItem {

    public static final int ANSWERED_EXTERNALLY_TYPE = 7;// android.provider.CallLog.Calls.ANSWERED_EXTERNALLY_TYPE;
    public static final int BLOCKED_TYPE = 6;// android.provider.CallLog.Calls.BLOCKED_TYPE;
    public static final int INCOMING_TYPE = 1;// android.provider.CallLog.Calls.INCOMING_TYPE;
    public static final int MISSED_TYPE = 3;// android.provider.CallLog.Calls.MISSED_TYPE;
    public static final int OUTGOING_TYPE = 2;// android.provider.CallLog.Calls.OUTGOING_TYPE;
    public static final int REJECTED_TYPE = 5;// android.provider.CallLog.Calls.REJECTED_TYPE;
    public static final int VOICEMAIL_TYPE = 4;// android.provider.CallLog.Calls.VOICEMAIL_TYPE;

    private String name;
    private String numberLabel;
    private int numberType;
    private long date;
    private long duration;
    private boolean read;
    private boolean acknowledged;
    private String number;
    private int type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumberLabel() {
        return numberLabel;
    }

    public void setNumberLabel(String numberLabel) {
        this.numberLabel = numberLabel;
    }

    public int getNumberType() {
        return numberType;
    }

    public void setNumberType(int numberType) {
        this.numberType = numberType;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public boolean isNew() {
        return acknowledged;
    }

    public void setNew(boolean acknowledged) {
        this.acknowledged = acknowledged;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
