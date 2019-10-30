package ru.hotmule.beaconfinder.utils

import android.content.Context
import kotlin.math.pow
import kotlin.math.roundToInt

fun Double.roundToTwoDecimals(): Double {
    val factor = 10.0.pow(2.0)
    return (this * factor).roundToInt() / factor
}

fun Float.dpToPx(context: Context) = this * context.resources.displayMetrics.density

fun Int.pxToDp(context: Context) = this / context.resources.displayMetrics.density