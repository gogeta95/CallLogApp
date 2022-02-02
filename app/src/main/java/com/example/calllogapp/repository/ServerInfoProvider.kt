package com.example.calllogapp.repository

import com.example.calllogapp.model.ServerInfo

const val PORT = 8000

interface ServerInfoProvider {

    fun getServerURl(): String

    fun setStartTime()

    fun getServerInfo(): ServerInfo
}