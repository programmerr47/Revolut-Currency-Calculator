package com.github.programmerr47.currencycalculator

import android.app.Activity
import android.content.Context
import android.util.TypedValue.COMPLEX_UNIT_PX
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.DimenRes
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

fun ViewGroup.inflate(@LayoutRes layoutId: Int, attachToRoot: Boolean = true) =
        context.inflate(layoutId, this, attachToRoot)

fun Context.inflate(@LayoutRes layoutId: Int, viewGroup: ViewGroup? = null, attachToRoot: Boolean = viewGroup != null) =
        LayoutInflater.from(this).inflate(layoutId, viewGroup, attachToRoot)

inline fun <T : View> Activity.bind(@IdRes id: Int, init: T.() -> Unit) =
        findViewById<T>(id).apply(init)

fun <T : View> RecyclerView.ViewHolder.bind(@IdRes id: Int) = itemView.findViewById<T>(id)

fun View.dimenInt(@DimenRes id: Int) = context.resources.getDimensionPixelSize(id)
fun View.dimen(@DimenRes id: Int) = context.resources.getDimension(id)

var TextView.textSizePx: Float
    get() = textSize
    set(px) = setTextSize(COMPLEX_UNIT_PX, px)

fun <T> Iterable<T>.move(item: T, newPos: Int): List<T> =
        mutableListOf<T>().also {
            filterNotTo(it) { it == item }
            it.add(newPos, item)
        }

fun RecyclerView.Adapter<*>.calculateDiff(callback: DiffUtil.Callback) =
        DiffUtil.calculateDiff(callback).dispatchUpdatesTo(this)