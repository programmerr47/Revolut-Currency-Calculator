package com.github.programmerr47.currencycalculator.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

fun broadcastReceiver(onReceive: (Context, Intent) -> Unit) = object : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) = onReceive(context, intent)
}