package com.krishnakandula.pulseview

import android.content.Context
import android.content.res.Resources

/**
 * Created by Krishna Chaitanya Kandula on 12/21/2017.
 */

fun getDisplayWidth(context: Context): Int = context.resources.displayMetrics.widthPixels

fun getDisplayHeight(context: Context): Int = context.resources.displayMetrics.heightPixels

fun Float.toDp(): Float = this / Resources.getSystem().displayMetrics.density

fun Float.toPx(): Float = this * Resources.getSystem().displayMetrics.density

fun Int.toDp(): Float = this / Resources.getSystem().displayMetrics.density

fun Int.toPx(): Float = this * Resources.getSystem().displayMetrics.density
