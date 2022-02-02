package com.example.calllogapp.interactor

import com.example.calllogapp.model.CallRecord
import com.example.calllogapp.repository.ContactsRepository
import com.example.calllogapp.repository.DataRepository
import com.example.calllogapp.utils.formatDate
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
* This class is not thread safe and it doesn't have to be. since you can only have 1 call at once.
 */
class CallLogInteractorImpl(
    private val contactsRepository: ContactsRepository,
    private val dataRepository: DataRepository,
    private val ioDispatcher: CoroutineDispatcher,
    private val currentCallQueryWatcher: CurrentCallQueryWatcher
): CallLogInteractor {

    private var currentCallStart: Long? = null
    private var currentCallerName: String? = null

    override fun callStarted(phoneNumber: String) {
        runOnDispatcher {
            currentCallStart = System.currentTimeMillis()
            currentCallerName = contactsRepository.getNameForNumber(phoneNumber)

            currentCallQueryWatcher.onCallStarted()
            dataRepository.setCurrentCall(phoneNumber, currentCallerName)
        }
    }

    override fun callEnded(phoneNumber: String) {
        val startTime = currentCallStart ?: return

        runOnDispatcher {
            dataRepository.callEnded()
            val endTime = System.currentTimeMillis()
            val duration = endTime - startTime

            val record = CallRecord(
                beginning = startTime.formatDate(),
                duration = TimeUnit.MILLISECONDS.toSeconds(duration).toString(),
                number = phoneNumber,
                name = currentCallerName,
                timesQueried = currentCallQueryWatcher.getCurrentCallQueryCount()
            )
            dataRepository.addCallRecord(record)

            currentCallStart = null
            currentCallerName = null
        }
    }

    private fun runOnDispatcher(block: CoroutineScope.() -> Unit) = CoroutineScope(ioDispatcher).launch {
        block()
    }
}