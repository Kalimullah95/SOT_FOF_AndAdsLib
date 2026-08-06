package com.manual.mediation.library.sotadlib.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WalkThroughItem(
    val heading: String = "",
    val description: String = "",
    val headingColor: Int = 0,
    val descriptionColor: Int = 0,
    val nextColor: Int = 0,
    val nextBackground: Int = 0,
    val drawableResId: Int = 0,
    val drawableBubbleResId: Int = 0,
    val viewBackgroundColor: Int = 0,
    val imageScale: Int = 0,
    val blurVisibility: Boolean = false,
) : Parcelable