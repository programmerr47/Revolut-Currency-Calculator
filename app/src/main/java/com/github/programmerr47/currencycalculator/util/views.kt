package com.github.programmerr47.currencycalculator.util

import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.DimenRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.LENGTH_LONG
import com.google.android.material.snackbar.Snackbar.LENGTH_SHORT

fun ViewGroup.inflate(@LayoutRes layoutId: Int, attachToRoot: Boolean = true) =
        context.inflate(layoutId, this, attachToRoot)

fun View.dimenInt(@DimenRes id: Int) = context.resources.getDimensionPixelSize(id)
fun View.dimen(@DimenRes id: Int) = context.resources.getDimension(id)

var TextView.textSizePx: Float
    get() = textSize
    set(px) = setTextSize(TypedValue.COMPLEX_UNIT_PX, px)

fun View.showSnackbar(@StringRes msgId: Int, length: Int = LENGTH_SHORT) = Snackbar.make(this, msgId, length).show()