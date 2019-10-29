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

import android.view.View;
import android.view.ViewGroup;

import com.github.android.removeduplicates.R;
import com.github.duplicates.DuplicateAdapter;

/**
 * List adapter for duplicate alarms.
 *
 * @author moshe.w
 */
public class AlarmAdapter extends DuplicateAdapter<AlarmItem, AlarmViewHolder> {

    @Override
    public AlarmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = createViewHolder(R.layout.same_alarm, parent, viewType);
        return new AlarmViewHolder(itemView, this);
    }
}
