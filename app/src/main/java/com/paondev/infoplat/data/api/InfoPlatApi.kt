package com.paondev.infoplat.data.api

import com.paondev.infoplat.model.History
import com.paondev.infoplat.BuildConfig
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Url

interface InfoPlatApi {

    @GET("api/provinces/all")
    suspend fun getProvinces(): ProvinceApiResponse
//
//    @GET("countries.json")
//    suspend fun getCountries(): List<Country>
//
//    @GET("categories.json")
//    suspend fun getCategories(): List<Category>
//
//    @GET("feeds.json")
//    suspend fun getFeeds(): List<Feed>
//
//    @GET("languages.json")
//    suspend fun getLanguages(): List<Language>
//
//    @GET("regions.json")
//    suspend fun getRegions(): List<Region>
//
//    @GET("streams.json")
//    suspend fun getStreams(): List<Stream>
//
//    @GET("logos.json")
//    suspend fun getLogos(): List<Logo>
//
//    @GET("blocklist.json")
//    suspend fun getBlocklist(): List<BlocklistItem>
//
//    @GET
//    suspend fun getM3UPlaylist(@Url url: String): ResponseBody

    @POST
    suspend fun getInfoPlatJateng(@Url url: String, @Body channel: History): Response<History>

    @POST
    suspend fun getJabarPajakInfo(
        @Url url: String = BuildConfig.JABAR_PAJAK_API_URL,
        @Body request: JabarPajakRequest,
        @Header("x-signature") signature: String = BuildConfig.JABAR_PAJAK_X_SIGNATURE,
        @Header("x-localization") localization: String = BuildConfig.JABAR_PAJAK_X_LOCALIZATION,
        @Header("api-key") apiKey: String = BuildConfig.JABAR_PAJAK_API_KEY,
        @Header("user-agen") userAgent: String = "Dart/3.9 (dart:io)"
    ): Response<JabarPajakResponse>
}
