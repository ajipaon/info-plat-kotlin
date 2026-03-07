package com.paondev.infoplat.data.api

import com.paondev.infoplat.BuildConfig
import com.paondev.infoplat.model.History
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Url

interface InfoPlatApi {

    @GET("api/provinces/all")
    suspend fun getProvinces(): ProvinceApiResponse

    @POST
    suspend fun getInfoPlatJateng(@Url url: String, @Body channel: History): Response<History>

    @POST
    suspend fun getJabarPajakInfo(
        @Url url: String = BuildConfig.JABAR_PAJAK_API_URL,
        @Body request: JabarPajakRequest,
        @Header("x-signature") signature: String,
        @Header("x-localization") localization: String,
        @Header("api-key") apiKey: String,
        @Header("user-agen") userAgent: String
    ): Response<JabarPajakResponse>

    @POST()
    suspend fun getJatimCaptcha(
        @Url url: String,
        @Header("origin") origin: String,
        @Header("referer") referer: String,
        @Header("user-agent") userAgent: String
    ): Response<JatimCaptchaResponse>

    @POST
    suspend fun verifyJatimCaptcha(
        @Url url: String,
        @Header("accept") accept: String,
        @Header("accept-language") acceptLanguage: String,
        @Header("origin") origin: String,
        @Header("referer") referer: String,
        @Header("user-agent") userAgent: String,
        @Header("x-app-token") appToken: String,
        @Header("x-requested-with") requestedWith: String,
        @Body request: JatimPkbRequest
    ): Response<JatimPkbResponse>

    @FormUrlEncoded
    @POST
    suspend fun getDiyPajakInfo(
        @Url url: String = BuildConfig.DIYP_PAJAK_API_URL,
        @Field("nomer") nomer: String,
        @Field("kode_belakang") kodeBelakang: String,
        @Header("Accept") accept: String,
        @Header("Accept-Encoding") acceptEncoding: String,
        @Header("Accept-Language") acceptLanguage: String,
        @Header("Content-Type") contentType: String,
        @Header("Origin") origin: String,
        @Header("Referer") referer: String,
        @Header("User-Agent") userAgent: String,
        @Header("X-Requested-With") requestedWith: String
    ): Response<DiypPajakResponse>

    @POST
    suspend fun getBantenPajakInfo(
        @Url url: String,
        @Body request: UniversalPajakRequest,
        @Header("Content-Type") contentType: String,
        @Header("x-token") authorization: String
    ): Response<BantenPajakResponse>

    @POST
    suspend fun getBaliPajakInfo(
        @Url url: String,
        @Body request: UniversalPajakRequest,
        @Header("x-token") authorization: String
    ): Response<BaliPajakResponse>

    @POST
    suspend fun getBangkaBelitungPajakInfo(
        @Url url: String,
        @Body request: UniversalPajakRequest,
        @Header("x-token") authorization: String
    ): Response<BangkaBelitungPajakResponse>

    @POST
    suspend fun getLampungPajakInfo(
        @Url url: String,
        @Body request: UniversalPajakRequest,
        @Header("x-token") authorization: String
    ): Response<LampungPajakResponse>

    @POST
    suspend fun getRiauPajakInfo(
        @Url url: String,
        @Body request:  UniversalPajakRequest,
        @Header("x-token") authorization: String
    ): Response<RiauPajakResponse>

    @POST
    suspend fun getSumbarPajakInfo(
        @Url url: String,
        @Body request: UniversalPajakRequest,
        @Header("Authorization") authorization: String
    ): Response<UniversalPajakResponse>

    @POST
    suspend fun solveOcr(
        @Url url: String,
        @Body request: OcrRequest
    ): Response<OcrResponse>

    @POST
    suspend fun getUniversalPajakInfo(
        @Url url: String,
        @Body request: UniversalPajakRequest,
        @Header("Authorization") authorization: String
    ): Response<UniversalPajakResponse>
}
