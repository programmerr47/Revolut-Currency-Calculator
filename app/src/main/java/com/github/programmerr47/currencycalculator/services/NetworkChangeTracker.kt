package com.github.programmerr47.currencycalculator.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import com.github.programmerr47.currencycalculator.util.broadcastReceiver
import io.reactivex.subjects.BehaviorSubject
import org.jetbrains.anko.connectivityManager

class NetworkChangeTracker(
        private val context: Context
) : Tracker, Emitter<Boolean> {
    private val networkSubject: BehaviorSubject<Boolean> by lazy { BehaviorSubject.createDefault(isOnline(context)) }
    private val receiver: BroadcastReceiver by lazy { broadcastReceiver { ctx, intent ->
        networkSubject.onNext(isOnline(ctx))
    } }

    override fun startTracking() {
        context.registerReceiver(receiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun stopTracking() {
        context.unregisterReceiver(receiver)
    }

    override fun observe() = networkSubject.hide()

    private fun isOnline(context: Context): Boolean {
        val networkInfo = context.connectivityManager.activeNetworkInfo
        return networkInfo?.isConnectedOrConnecting ?: false
    }
}