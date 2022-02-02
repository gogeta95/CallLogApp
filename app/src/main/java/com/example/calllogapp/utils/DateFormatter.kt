package com.example.calllogapp.utils

import java.text.SimpleDateFormat
import java.util.*

private val dateFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US)

fun Long.formatDate() = dateFormatter.format(Date(this)).let {
    StringBuilder(it).insert(it.length - 2, ":").toString()
}