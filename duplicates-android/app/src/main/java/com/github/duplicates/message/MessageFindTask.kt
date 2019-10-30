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

import android.content.Context

import com.github.duplicates.DuplicateFindTask
import com.github.duplicates.DuplicateTaskListener

/**
 * Task to find duplicate messages.
 *
 * @author moshe.w
 */
class MessageFindTask(context: Context, listener: DuplicateTaskListener<MessageItem, *>) : DuplicateFindTask<MessageItem, MessageViewHolder>(context, listener) {

    override fun createProvider(context: Context): MessageProvider {
        return MessageProvider(context)
    }

    override fun createAdapter(): MessageAdapter {
        return MessageAdapter()
    }

    override fun createComparator(): MessageComparator {
        return MessageComparator()
    }
}
