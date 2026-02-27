package com.paondev.infoplat.data.api

import com.paondev.infoplat.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val BASE_URL = "https://info-plat.ajisetiawan883.workers.dev/"

    private val okHttpClient: OkHttpClient by lazy {
        val builder = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)

        // Add Chucker interceptor for debug builds only
        if (BuildConfig.DEBUG) {
            val chuckerInterceptor = com.chuckerteam.chucker.api.ChuckerInterceptor.Builder(
                com.paondev.infoplat.InfoPlatApplication.instance
            )
                .maxContentLength(250000L)
                .redactHeaders(emptySet())
                .alwaysReadResponseBody(true)
                .build()

            builder.addInterceptor(chuckerInterceptor)

            // Also add HTTP logging interceptor
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            builder.addInterceptor(loggingInterceptor)
        }

        builder.build()
    }

    val apiService: InfoPlatApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(InfoPlatApi::class.java)
    }
}
