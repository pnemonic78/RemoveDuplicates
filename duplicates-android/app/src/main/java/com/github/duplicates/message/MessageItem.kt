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
package com.github.duplicates.message

import android.provider.Telephony
import com.github.duplicates.DuplicateItem

/**
 * Duplicate message item.
 *
 * @author moshe.w
 */
class MessageItem : DuplicateItem() {

    var address: String? = null
    var body: String? = null
        set(value) {
            var body = value
            if (body != null) {
                body = body.replace("\\s+".toRegex(), " ")
            }
            field = body
        }
    var dateReceived: Long = 0
    var dateSent: Long = 0
    var errorCode: Int = 0
    var isLocked: Boolean = false
    var person: Int = 0
    var protocol: Int = 0
    var isRead: Boolean = false
    var isSeen: Boolean = false
    var status = STATUS_NONE
    var subject: String? = null
    var threadId: Long = 0
    var type = MESSAGE_TYPE_ALL

    override fun contains(s: CharSequence): Boolean {
        return (address?.contains(s, true) ?: false)
            || (body?.contains(s, true) ?: false)
            || (subject?.contains(s, true) ?: false)
    }

    companion object {

        /**
         * Message type: all messages.
         */
        const val MESSAGE_TYPE_ALL = Telephony.TextBasedSmsColumns.MESSAGE_TYPE_ALL

        /**
         * Message type: inbox.
         */
        const val MESSAGE_TYPE_INBOX = Telephony.TextBasedSmsColumns.MESSAGE_TYPE_INBOX

        /**
         * Message type: sent messages.
         */
        const val MESSAGE_TYPE_SENT = Telephony.TextBasedSmsColumns.MESSAGE_TYPE_SENT

        /**
         * Message type: drafts.
         */
        const val MESSAGE_TYPE_DRAFT = Telephony.TextBasedSmsColumns.MESSAGE_TYPE_DRAFT

        /**
         * Message type: outbox.
         */
        const val MESSAGE_TYPE_OUTBOX = Telephony.TextBasedSmsColumns.MESSAGE_TYPE_OUTBOX

        /**
         * Message type: failed outgoing message.
         */
        const val MESSAGE_TYPE_FAILED = Telephony.TextBasedSmsColumns.MESSAGE_TYPE_FAILED

        /**
         * Message type: queued to send later.
         */
        const val MESSAGE_TYPE_QUEUED = Telephony.TextBasedSmsColumns.MESSAGE_TYPE_QUEUED

        const val STATUS_NONE = Telephony.TextBasedSmsColumns.STATUS_NONE
    }
}
