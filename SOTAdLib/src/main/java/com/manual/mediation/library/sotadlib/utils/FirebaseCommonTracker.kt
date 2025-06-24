package com.manual.mediation.library.sotadlib.utils

import android.content.Context
import android.util.Log
import com.manual.mediation.library.sotadlib.interfaces.CommonEventTracker

class FirebaseCommonTracker : CommonEventTracker {
    override fun logEvent(context: Context, eventName: String, params: Map<String, Any>) {
        //CustomFirebaseEvents.logEvent(context, eventName, params)
        Log.d("logEvent", "logEvent:$eventName ")
    }
}