package com.example.calllogapp

import com.example.calllogapp.model.CallRecord

fun generateCallRecords(n: Int) = (1..n).map { i ->
    CallRecord(
        beginning = "beginning $i",
        duration = "duration $i",
        number = "number $i",
        name = "name $i",
        timesQueried = i
    )
}