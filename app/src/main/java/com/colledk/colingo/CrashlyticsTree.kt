package com.colledk.colingo

import android.util.Log
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import timber.log.Timber

class CrashlyticsTree: Timber.Tree() {
    override fun isLoggable(tag: String?, priority: Int): Boolean {
        return priority == Log.WARN || priority == Log.ERROR
    }

    override fun log(
        priority: Int,
        tag: String?,
        message: String,
        t: Throwable?
    ) {
        if (isLoggable(tag, priority)) {
            Firebase.crashlytics.log("$tag: $message")
            t?.let(Firebase.crashlytics::recordException)
        }
    }
}