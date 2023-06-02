package com.example.mymovies.data.remote.client

interface ApiServiceFactory {
    fun <T> create(serviceClass: Class<T>): T
}

inline fun <reified T> ApiServiceFactory.create(): T = create(T::class.java)