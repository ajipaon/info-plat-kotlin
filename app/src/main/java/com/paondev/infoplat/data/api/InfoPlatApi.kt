package com.paondev.infoplat.data.api

import com.paondev.infoplat.model.History
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

interface InfoPlatApi {

    @GET("api/provinces/all")
    suspend fun getProvinces(): List<?>
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
}
