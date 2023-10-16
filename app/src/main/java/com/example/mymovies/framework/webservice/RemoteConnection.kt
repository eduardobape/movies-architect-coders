package com.example.mymovies.framework.webservice

import com.example.mymovies.App
import com.example.mymovies.BuildConfig
import com.example.mymovies.appContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

object RemoteConnection {

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
            if (!NetworkChecker.isOnline(application.appContext)) {
                throw NoInternetConnectionException("No Internet connection")
            }
            chain.proceed(chain.request().newBuilder().build())
        }
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(MoviesWebServiceDataSource.apiBaseUrl)
        .client(networkClient)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    val moviesApiServices: MoviesSearchApiService = retrofit.create()
}
