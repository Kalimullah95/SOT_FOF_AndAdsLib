package com.manual.mediation.library.sotadlib.objects

import android.graphics.Color

import androidx.annotation.ColorInt

object StatusBarColor {
    var statusBarColor: Int = Color.WHITE
        private set

    fun setColor(@ColorInt color: Int) {
        statusBarColor = color
    }

    fun getColor(): Int {
        return statusBarColor
    }
}