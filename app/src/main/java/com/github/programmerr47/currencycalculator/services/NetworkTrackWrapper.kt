package com.github.programmerr47.currencycalculator.services

class NetworkTrackWrapper(
        private val networkTracker: NetworkChangeTracker,
        private val hasNetListener: () -> Unit,
        private val noNetListener: () -> Unit
) : Tracker {
    private val tracker: Tracker by lazy { DisposableTracker { createDisposableTracker() } }

    override fun startTracking() {
        tracker.startTracking()
        networkTracker.startTracking()
    }

    override fun stopTracking() {
        networkTracker.stopTracking()
        tracker.stopTracking()
    }

    private fun createDisposableTracker() = networkTracker.observe()
            .distinctUntilChanged()
            .subscribe {
                if (it) hasNetListener() else noNetListener()
            }
}