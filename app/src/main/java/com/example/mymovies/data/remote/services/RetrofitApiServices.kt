package com.example.mymovies.data.remote.services

import com.example.mymovies.App
import com.example.mymovies.BuildConfig
import com.example.mymovies.appContext
import com.example.mymovies.data.errors.NoInternetConnectionException
import com.example.mymovies.data.remote.apiurls.ApiUrlsManager
import com.example.mymovies.data.remote.utils.NetworkUtil
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitApiServices : ApiServicesFactory {

    private lateinit var application: App

    fun init(app: App) {
        application = app
    }

    private val networkClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val originalRequest = chain.request()
            val modifiedRequest =
                originalRequest.newBuilder().addHeader("Authorization", "Bearer ${BuildConfig.TMDB_ACCESS_TOKEN}")
                    .build()
            chain.proceed(modifiedRequest)
        }
        .addInterceptor { chain ->
            if (!NetworkUtil.isOnline(application.appContext)) {
                throw NoInternetConnectionException("No Internet connection")
            }
            chain.proceed(chain.request().newBuilder().build())
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
