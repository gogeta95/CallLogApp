package com.example.calllogapp.model

import com.google.gson.annotations.SerializedName

data class ServerInfo(
    @SerializedName("start")
    val start: String,
    @SerializedName("services")
    val services: List<Service>
)