package com.github.duplicates

import android.app.Application
import com.github.android.removeduplicates.BuildConfig
import com.github.duplicates.db.DuplicatesDatabase
import com.github.util.LogTree
import timber.log.Timber

class DuplicateApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        Timber.plant(LogTree(BuildConfig.DEBUG))
    }

    override fun onTerminate() {
        super.onTerminate()
        DuplicatesDatabase.getDatabase(this).close()
    }
}