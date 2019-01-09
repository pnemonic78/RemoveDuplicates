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

import android.content.Context;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.github.android.removeduplicates.BuildConfig;
import com.github.android.removeduplicates.R;
import com.github.duplicates.DuplicateItemPair;
import com.github.duplicates.DuplicateViewHolder;

import java.util.Date;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.github.duplicates.alarm.AlarmComparator.ALARM_TIME;
import static com.github.duplicates.alarm.AlarmComparator.ALERT_TIME;
import static com.github.duplicates.alarm.AlarmComparator.NAME;
import static com.github.duplicates.alarm.AlarmComparator.REPEAT;

/**
 * View holder of a duplicate alarms.
 *
 * @author moshe.w
 */
public class AlarmViewHolder extends DuplicateViewHolder<AlarmItem> {

    @BindView(R.id.match)
    TextView match;

    @BindView(R.id.checkbox1)
    CheckBox checkbox1;
    @BindView(R.id.alarm1)
    TextView alarm1;
    @BindView(R.id.alert1)
    TextView alert1;
    @BindView(R.id.repeat1)
    TextView repeat1;
    @BindView(R.id.name1)
    TextView name1;

    @BindView(R.id.checkbox2)
    CheckBox checkbox2;
    @BindView(R.id.alarm2)
    TextView alarm2;
    @BindView(R.id.alert2)
    TextView alert2;
    @BindView(R.id.repeat2)
    TextView repeat2;
    @BindView(R.id.name2)
    TextView name2;

    public AlarmViewHolder(View itemView) {
        this(itemView, null);
    }

    public AlarmViewHolder(View itemView, OnItemCheckedChangeListener onCheckedChangeListener) {
        super(itemView, onCheckedChangeListener);
        ButterKnife.bind(this, itemView);
    }

    @Override
    protected void bindHeader(Context context, DuplicateItemPair<AlarmItem> pair) {
        match.setText(context.getString(R.string.match, percentFormatter.format(pair.getMatch())));
    }

    @Override
    protected void bindItem1(Context context, AlarmItem item) {
        checkbox1.setChecked(item.isChecked());
        checkbox1.setText(BuildConfig.DEBUG ? Long.toString(item.getId()) : "");
        alarm1.setText(formatHourMinutes(context, item.getAlarmTime()));
        alert1.setText(DateUtils.formatDateTime(context, item.getAlertTime(), DateUtils.FORMAT_SHOW_TIME));
        name1.setText(item.getName());
        repeat1.setText(formatRepeat(context, item));
    }

    @Override
    protected void bindItem2(Context context, AlarmItem item) {
        checkbox2.setChecked(item.isChecked());
        checkbox2.setText(BuildConfig.DEBUG ? Long.toString(item.getId()) : "");
        alarm2.setText(formatHourMinutes(context, item.getAlarmTime()));
        alert2.setText(DateUtils.formatDateTime(context, item.getAlertTime(), DateUtils.FORMAT_SHOW_TIME));
        name2.setText(item.getName());
        repeat2.setText(formatRepeat(context, item));
    }

    @Override
    protected void bindDifference(Context context, DuplicateItemPair<AlarmItem> pair) {
        boolean[] difference = pair.getDifference();

        bindDifference(alarm1, alarm2, difference[ALARM_TIME]);
        bindDifference(alert1, alert2, difference[ALERT_TIME]);
        bindDifference(name1, name2, difference[NAME]);
        bindDifference(repeat1, repeat2, difference[REPEAT]);
    }

    @OnClick(R.id.checkbox1)
    void checkbox1Clicked() {
        if (onCheckedChangeListener != null) {
            onCheckedChangeListener.onItemCheckedChangeListener(item1, checkbox1.isChecked());
        }
    }

    @OnClick(R.id.checkbox2)
    void checkbox2Clicked() {
        if (onCheckedChangeListener != null) {
            onCheckedChangeListener.onItemCheckedChangeListener(item2, checkbox2.isChecked());
        }
    }

    protected CharSequence formatHourMinutes(Context context, int alarmTime) {
        long hours = alarmTime / 100;
        long minutes = alarmTime % 100;
        long time = (hours * DateUtils.HOUR_IN_MILLIS) + (minutes * DateUtils.MINUTE_IN_MILLIS);
        java.text.DateFormat format = DateFormat.getTimeFormat(context);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        return format.format(new Date(time));
    }

    protected CharSequence formatRepeat(Context context, AlarmItem item) {
        return item.getRepeat().toString(context, true);
    }
}
