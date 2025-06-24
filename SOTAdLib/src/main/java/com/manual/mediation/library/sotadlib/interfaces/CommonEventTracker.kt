package com.manual.mediation.library.sotadlib.interfaces

import android.content.Context

interface CommonEventTracker {
    fun logEvent(
        context: Context,
        eventName: String,
        params: Map<String, Any> = emptyMap()
    )
}