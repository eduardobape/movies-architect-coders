package com.example.mymovies.data.remoteapi.apiclient

interface ApiServiceFactory {
    fun <T> create(serviceClass: Class<T>): T
}

inline fun <reified T> ApiServiceFactory.create(): T = create(T::class.java)