package com.example.calllogapp.repository

import android.app.Application
import android.net.wifi.WifiManager
import android.text.format.Formatter
import androidx.appcompat.app.AppCompatActivity
import com.example.calllogapp.model.ServerInfo
import com.example.calllogapp.model.Service
import com.example.calllogapp.utils.formatDate

class ServerInfoProviderImpl(private val application: Application) : ServerInfoProvider {

    private lateinit var serverInfo: ServerInfo

    override fun getServerURl(): String {
        return "${getCurrentIp(application)}:$PORT"
    }

    override fun setStartTime() {
        val services = endpoints.map { endpoint ->
            Service(name = endpoint, uri = "http://${getServerURl()}/$endpoint")
        }
        serverInfo = ServerInfo(
            start = System.currentTimeMillis().formatDate(),
            services = services
        )
    }

    override fun getServerInfo() = serverInfo

    private fun getCurrentIp(application: Application): String {
        val wifiMgr: WifiManager =
            application.getSystemService(AppCompatActivity.WIFI_SERVICE) as WifiManager
        val ip: Int = wifiMgr.connectionInfo.ipAddress
        return Formatter.formatIpAddress(ip)
    }

    companion object {
        private val endpoints = listOf("status", "log")
    }
}