package com.example.calllogapp.interactor

/**
 * This class is not thread safe and it doesn't have to be. since you can only have 1 call at once.
 */
class CurrentCallQueryWatcherImpl: CurrentCallQueryWatcher {

    private var count = 0

    override fun onCallStarted() {
        count = 0
    }

    override fun onCurrentCallRequested() {
        count++
    }

    override fun getCurrentCallQueryCount() = count
}