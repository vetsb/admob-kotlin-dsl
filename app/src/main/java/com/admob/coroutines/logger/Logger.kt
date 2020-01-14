package com.admob.coroutines.logger

import android.util.Log
import com.admob.coroutines.AdmobCoroutines

private const val LOG_TAG = "AdmobCoroutines"

fun log(msg: String) {
    if (AdmobCoroutines.isLoggingEnabled) {
        Log.d(LOG_TAG, msg)
    }
}