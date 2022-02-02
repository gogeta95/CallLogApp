package com.example.calllogapp.model

import com.google.gson.annotations.SerializedName

data class CurrentCall(
    @SerializedName("ongoing")
    val ongoing: Boolean,
    @SerializedName("number")
    val number: String? = null,
    @SerializedName("name")
    val name: String? = null
)