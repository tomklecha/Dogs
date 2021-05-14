package com.tkdev.dogs.common

import android.net.ConnectivityManager
import android.net.NetworkCapabilities

interface ConnectionManager {
    fun checkConnection(): Boolean
}

class ConnectionManagerDefault(private val connectivityManager: ConnectivityManager) :
    ConnectionManager {
    override fun checkConnection(): Boolean {
        val network = connectivityManager.activeNetwork
        val connection = connectivityManager.getNetworkCapabilities(network)

        return connection != null &&
                (connection.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        connection.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
    }
}