package com.manual.mediation.library.sotadlib.utils

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.annotation.ColorInt


fun Activity.hideSystemUIUpdated() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        window.insetsController?.let { controller ->
            controller.hide(WindowInsets.Type.navigationBars())
            controller.systemBarsBehavior =
                WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    } else {
        @Suppress("DEPRECATION")
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )
    }
}

fun Activity.setStatusBarColor(@ColorInt color: Int) {
    window.statusBarColor = color

    val isLight = isColorLight(color)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        // Android 11+ - Use WindowInsetsController
        val insetsController = window.decorView.windowInsetsController
        insetsController?.setSystemBarsAppearance(
            if (isLight) WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS else 0,
            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
        )
    } else {
        @Suppress("DEPRECATION")
        var flags = window.decorView.systemUiVisibility
        flags = if (isLight) {
            flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            flags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        }
        window.decorView.systemUiVisibility = flags
    }
}



// Helper function to determine if a color is light
private fun isColorLight(@ColorInt color: Int): Boolean {
    val r = (color shr 16) and 0xFF
    val g = (color shr 8) and 0xFF
    val b = color and 0xFF
    val brightness = (r * 299 + g * 587 + b * 114) / 1000
    return brightness >= 180
}
