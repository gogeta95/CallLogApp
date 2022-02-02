package com.example.calllogapp.interactor

import junit.framework.Assert.assertEquals
import org.junit.Test


class CurrentCallQueryWatcherImplTest {

    private val watcher = CurrentCallQueryWatcherImpl()

    @Test
    fun `WHEN data requested THEN count updated`(){
        testQueryCount(4)
        testQueryCount(6)
        testQueryCount(10)
    }

    private fun testQueryCount(count: Int) {
        watcher.onCallStarted()
        repeat(count) {
            watcher.onCurrentCallRequested()
        }
        assertEquals(count, watcher.getCurrentCallQueryCount())
    }
}