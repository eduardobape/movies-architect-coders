package com.example.mymovies.data.webservice

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

object NetworkChecker {
    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val accessNetworkState = connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
        val result = when {
            accessNetworkState.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            accessNetworkState.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            accessNetworkState.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
        return result
    }
}
