package com.github.programmerr47.currencycalculator.util

import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.annotation.DimenRes
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.LENGTH_SHORT
import org.jetbrains.anko.inputMethodManager

fun View.dimenInt(@DimenRes id: Int) = context.resources.getDimensionPixelSize(id)
fun View.dimen(@DimenRes id: Int) = context.resources.getDimension(id)

var TextView.textSizePx: Float
    get() = textSize
    set(px) = setTextSize(TypedValue.COMPLEX_UNIT_PX, px)

fun View.showSnackbar(@StringRes msgId: Int, length: Int = LENGTH_SHORT) = Snackbar.make(this, msgId, length).show()

fun View.hideKeyboard() {
    context.inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    clearFocus()
}
