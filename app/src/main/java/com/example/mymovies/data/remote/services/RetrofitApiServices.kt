package com.example.mymovies.data.remote.services

import com.example.mymovies.BuildConfig
import com.example.mymovies.data.remote.apiurls.ApiUrlsManager
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitApiServices : ApiServicesFactory {
    private val networkClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val originalRequest = chain.request()
            val modifiedRequest =
                originalRequest.newBuilder().addHeader("Authorization", "Bearer ${BuildConfig.TMDB_ACCESS_TOKEN}")
                    .build()
            chain.proceed(modifiedRequest)
        }
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(ApiUrlsManager.apiBaseUrl)
        .client(networkClient)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    override fun <T> create(serviceClass: Class<T>): T {
        return retrofit.create(serviceClass)
    }
}
