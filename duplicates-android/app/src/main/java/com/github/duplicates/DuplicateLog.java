package com.github.duplicates;

import android.util.Log;

/**
 * Logger.
 */
public class DuplicateLog {

    public static void e(String tag, String msg, Throwable e) {
        Log.e(tag, msg, e);
    }

}
