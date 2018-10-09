package com.github.programmerr47.currencycalculator.util

import android.view.View
import androidx.annotation.IdRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

fun <T : View> RecyclerView.ViewHolder.bind(@IdRes id: Int) = itemView.findViewById<T>(id)

fun RecyclerView.Adapter<*>.calculateDiff(callback: DiffUtil.Callback) =
        DiffUtil.calculateDiff(callback).dispatchUpdatesTo(this)