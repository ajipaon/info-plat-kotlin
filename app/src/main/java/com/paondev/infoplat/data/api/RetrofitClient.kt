package com.paondev.infoplat.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://info-plat.ajisetiawan883.workers.dev/"

    val apiService: InfoPlatApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(InfoPlatApi::class.java)
    }
}
