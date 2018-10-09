package com.github.programmerr47.currencycalculator.util

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes

fun Context.inflate(@LayoutRes layoutId: Int, viewGroup: ViewGroup? = null, attachToRoot: Boolean = viewGroup != null) =
        LayoutInflater.from(this).inflate(layoutId, viewGroup, attachToRoot)

fun <T : View> Activity.bindable(@IdRes id: Int) = lazy { findViewById<T>(id) }