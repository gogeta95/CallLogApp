package com.example.calllogapp.model

import com.google.gson.annotations.SerializedName

data class Service(
    @SerializedName("name")
    val name: String,
    @SerializedName("uri")
    val uri: String
)
