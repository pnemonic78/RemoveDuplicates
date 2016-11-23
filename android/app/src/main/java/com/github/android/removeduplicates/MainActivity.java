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
package com.github.android.removeduplicates;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageButton;
import android.widget.Spinner;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;

/**
 * Main activity.
 *
 * @author moshe.w
 */
public class MainActivity extends Activity {

    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.spinner_action)
    ImageButton spinnerAction;
    @BindView(android.R.id.list)
    RecyclerView list;

    private AsyncTask<Void, Void, Void> task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        spinner.setAdapter(new MainSpinnerAdapter());
    }

    @OnItemSelected(R.id.spinner)
    void onItemSelected(Spinner spinner, int position) {
    }

    @OnClick(R.id.spinner_action)
    void onActionClicked() {
        if (task != null) {
            task.cancel(true);
            spinnerAction.setImageResource(android.R.drawable.ic_media_play);
        } else {
            spinnerAction.setImageResource(android.R.drawable.ic_media_pause);
            MainSpinnerItem item = (MainSpinnerItem) spinner.getSelectedItem();
            removeDuplicates(item);
            task = new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    spinnerAction.setImageResource(android.R.drawable.ic_media_play);
                    task = null;
                }
            };
            task.execute();
        }
    }

    private void removeDuplicates(MainSpinnerItem item) {
        switch (item) {
            case ALARMS:
                //TODO implement me!
                break;
            case BOOKMARKS:
                //TODO implement me!
                break;
            case CALENDAR:
                //TODO implement me!
                break;
            case CALL_LOG:
                //TODO implement me!
                break;
            case CONTACTS:
                //TODO implement me!
                break;
            case MESSAGES:
                //TODO implement me!
                break;
        }
    }
}
