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

import android.content.Context;
import android.os.Build;

import com.github.duplicates.WrapperProvider;
import com.github.duplicates.DuplicateProvider;

/**
 * Provide duplicate messages.
 *
 * @author moshe.w
 */
public class MessageProvider extends WrapperProvider<MessageItem> {

    public MessageProvider(Context context) {
        super(context);
    }

    @Override
    protected DuplicateProvider<MessageItem> createDelegate(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return new JellybeanMessageProvider(context);
        }
        return new KitkatMessageProvider(context);
    }
}
