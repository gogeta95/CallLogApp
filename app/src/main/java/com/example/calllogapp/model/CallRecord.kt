package com.example.calllogapp.model

import com.google.gson.annotations.SerializedName

data class CallRecord(
    @SerializedName("beginning")
    val beginning: String,
    @SerializedName("duration")
    val duration: String,
    @SerializedName("number")
    val number: String,
    @SerializedName("name")
    val name: String?,
    @SerializedName("timesQueried")
    val timesQueried: Int
)