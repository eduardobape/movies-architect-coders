package com.example.mymovies.data.remote.services

interface ApiServicesFactory {
    fun <T> create(serviceClass: Class<T>): T
}

inline fun <reified T> ApiServicesFactory.create(): T = create(T::class.java)
