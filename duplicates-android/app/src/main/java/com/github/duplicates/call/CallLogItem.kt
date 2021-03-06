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
package com.github.duplicates.call

import com.github.duplicates.DuplicateItem
import com.github.duplicates.DuplicateItemType

/**
 * Duplicate call log entry.
 *
 * @author moshe.w
 */
class CallLogItem : DuplicateItem(DuplicateItemType.CALL_LOG) {

    var name: String? = null
    var numberLabel: String? = null
    var numberType: Int = 0
    var date: Long = 0
    var duration: Long = 0
    var isRead: Boolean = false
    var isNew: Boolean = false
    var number: String? = null
    var type: Int = 0

    override fun contains(s: CharSequence): Boolean {
        return (name?.contains(s, true) ?: false)
            || (number?.contains(s, true) ?: false)
            || (numberLabel?.contains(s, true) ?: false)
    }

    companion object {

        // android.provider.CallLog.Calls.ANSWERED_EXTERNALLY_TYPE; (Added in API level 25)
        const val ANSWERED_EXTERNALLY_TYPE = 7

        // android.provider.CallLog.Calls.BLOCKED_TYPE; (Added in API level 24)
        const val BLOCKED_TYPE = 6
        const val INCOMING_TYPE = android.provider.CallLog.Calls.INCOMING_TYPE
        const val MISSED_TYPE = android.provider.CallLog.Calls.MISSED_TYPE
        const val OUTGOING_TYPE = android.provider.CallLog.Calls.OUTGOING_TYPE

        // android.provider.CallLog.Calls.REJECTED_TYPE; (Added in API level 24)
        const val REJECTED_TYPE = 5

        // android.provider.CallLog.Calls.VOICEMAIL_TYPE; (Added in API level 21)
        const val VOICEMAIL_TYPE = 4
    }
}
