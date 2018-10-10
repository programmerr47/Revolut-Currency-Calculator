package com.github.programmerr47.currencycalculator.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import java.math.BigDecimal
import java.math.BigDecimal.ZERO

fun broadcastReceiver(onReceive: (Context, Intent) -> Unit) = object : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) = onReceive(context, intent)
}

fun bigDecimal(representation: String, fallback: BigDecimal = ZERO) = try {
    BigDecimal(representation)
} catch (e: NumberFormatException) {
    fallback
}