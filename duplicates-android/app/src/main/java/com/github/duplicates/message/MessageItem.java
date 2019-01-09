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
package com.github.duplicates.message;

import android.text.TextUtils;

import com.github.duplicates.DuplicateItem;

/**
 * Duplicate message item.
 *
 * @author moshe.w
 */
public class MessageItem extends DuplicateItem {

    /**
     * Message type: all messages.
     */
    public static final int MESSAGE_TYPE_ALL = 0;//TextBasedSmsColumns.MESSAGE_TYPE_ALL
    /**
     * Message type: inbox.
     */
    public static final int MESSAGE_TYPE_INBOX = 1;//TextBasedSmsColumns.MESSAGE_TYPE_INBOX
    /**
     * Message type: sent messages.
     */
    public static final int MESSAGE_TYPE_SENT = 2;//TextBasedSmsColumns.MESSAGE_TYPE_SENT
    /**
     * Message type: drafts.
     */
    public static final int MESSAGE_TYPE_DRAFT = 3;//TextBasedSmsColumns.MESSAGE_TYPE_DRAFT
    /**
     * Message type: outbox.
     */
    public static final int MESSAGE_TYPE_OUTBOX = 4;//TextBasedSmsColumns.MESSAGE_TYPE_OUTBOX
    /**
     * Message type: failed outgoing message.
     */
    public static final int MESSAGE_TYPE_FAILED = 5;//TextBasedSmsColumns.MESSAGE_TYPE_FAILED
    /**
     * Message type: queued to send later.
     */
    public static final int MESSAGE_TYPE_QUEUED = 6;//TextBasedSmsColumns.MESSAGE_TYPE_QUEUED

    public static final int STATUS_NONE = -1;// TextBasedSmsColumns.STATUS_NONE

    private String address;
    private String body;
    private long dateReceived;
    private long dateSent;
    private int errorCode;
    private boolean locked;
    private int person;
    private int protocol;
    private boolean read;
    private boolean seen;
    private int status = STATUS_NONE;
    private String subject;
    private long threadId;
    private int type = MESSAGE_TYPE_ALL;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        if (body != null) {
            body = body.replaceAll("\\s+", " ");
        }
        this.body = body;
    }

    public long getDateReceived() {
        return dateReceived;
    }

    public void setDateReceived(long dateReceived) {
        this.dateReceived = dateReceived;
    }

    public long getDateSent() {
        return dateSent;
    }

    public void setDateSent(long dateSent) {
        this.dateSent = dateSent;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public int getPerson() {
        return person;
    }

    public void setPerson(int person) {
        this.person = person;
    }

    public int getProtocol() {
        return protocol;
    }

    public void setProtocol(int protocol) {
        this.protocol = protocol;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public long getThreadId() {
        return threadId;
    }

    public void setThreadId(long threadId) {
        this.threadId = threadId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public boolean contains(CharSequence s) {
        return (!TextUtils.isEmpty(address) && address.contains(s))
                || (!TextUtils.isEmpty(body) && body.contains(s))
                || (!TextUtils.isEmpty(subject) && subject.contains(s));
    }
}
