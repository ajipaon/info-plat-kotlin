package com.paondev.infoplat.data.api

import com.paondev.infoplat.model.History
import com.paondev.infoplat.BuildConfig
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
        @Header("x-signature") signature: String = BuildConfig.JABAR_PAJAK_X_SIGNATURE,
        @Header("x-localization") localization: String = BuildConfig.JABAR_PAJAK_X_LOCALIZATION,
        @Header("api-key") apiKey: String = BuildConfig.JABAR_PAJAK_API_KEY,
        @Header("user-agen") userAgent: String = "Dart/3.9 (dart:io)"
    ): Response<JabarPajakResponse>

    @POST()
    suspend fun getJatimCaptcha(
        @Url url: String = BuildConfig.JATIM_CAPTCHA_URL,
        @Header("origin") origin : String  = "https://bapenda.jatimprov.go.id",
        @Header("referer") referer : String  = "https://bapenda.jatimprov.go.id/info/pkb",
        @Header("user-agent") userAgent: String = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36"
    ): Response<JatimCaptchaResponse>

    @POST
    suspend fun verifyJatimCaptcha(
        @Url url: String = BuildConfig.JATIM_PAJAK_API_URL,
        @Header("accept") accept: String = "application/json",
        @Header("accept-language") acceptLanguage: String = "en-US,en;q=0.6",
        @Header("origin") origin: String = "https://bapenda.jatimprov.go.id",
        @Header("referer") referer: String = "https://bapenda.jatimprov.go.id/info/pkb",
        @Header("user-agent") userAgent: String = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36",
        @Header("x-app-token") appToken: String = "bapenda-jatim-info-layanan-2025",
        @Header("x-requested-with") requestedWith: String = "XMLHttpRequest",
        @Body request: JatimPkbRequest
    ): Response<JatimPkbResponse>

    @FormUrlEncoded
    @POST
    suspend fun getDiypPajakInfo(
        @Url url: String = BuildConfig.DIYP_PAJAK_API_URL,
        @Field("nomer") nomer: String,
        @Field("kode_belakang") kodeBelakang: String,
        @Header("Accept") accept: String = "application/json, text/javascript, */*; q=0.01",
        @Header("Accept-Encoding") acceptEncoding: String = "gzip, deflate, br, zstd",
        @Header("Accept-Language") acceptLanguage: String = "en-US,en;q=0.6",
        @Header("Content-Type") contentType: String = "application/x-www-form-urlencoded; charset=UTF-8",
        @Header("Origin") origin: String = "https://samsatsleman.jogjaprov.go.id",
        @Header("Referer") referer: String = "https://samsatsleman.jogjaprov.go.id/cek/pajak",
        @Header("User-Agent") userAgent: String = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36",
        @Header("X-Requested-With") requestedWith: String = "XMLHttpRequest"
    ): Response<DiypPajakResponse>

    @POST
    suspend fun getBantenPajakInfo(
        @Url url: String = BuildConfig.API_URL_INFO_PLAT,
        @Body request: BantenPajakRequest,
        @Header("Content-Type") contentType: String = "application/json"
    ): Response<BantenPajakResponse>

    @POST
    suspend fun getBaliPajakInfo(
        @Url url: String = "https://info-plat.ajisetiawan883.workers.dev/api/plat/check",
        @Body request: BaliPajakRequest
    ): Response<BaliPajakResponse>

    @POST
    suspend fun getBangkaBelitungPajakInfo(
        @Url url: String = "https://info-plat.ajisetiawan883.workers.dev/api/plat/check",
        @Body request: BangkaBelitungPajakRequest
    ): Response<BangkaBelitungPajakResponse>

    @POST
    suspend fun getLampungPajakInfo(
        @Url url: String = "https://info-plat.ajisetiawan883.workers.dev/api/plat/check",
        @Body request: LampungPajakRequest
    ): Response<LampungPajakResponse>

    @POST
    suspend fun getRiauPajakInfo(
        @Url url: String = "https://info-plat.ajisetiawan883.workers.dev/api/plat/check",
        @Body request: RiauPajakRequest
    ): Response<RiauPajakResponse>

    @POST
    suspend fun getSumbarPajakInfo(
        @Url url: String = "https://info-plat.ajisetiawan883.workers.dev/api/plat/check",
        @Body request: UniversalPajakRequest
    ): Response<SumbarPajakResponse>

    @POST
    suspend fun solveOcr(
        @Url url: String = "https://info-plat.ajisetiawan883.workers.dev/api/ocr",
        @Body request: OcrRequest
    ): Response<OcrResponse>

    @POST
    suspend fun getUniversalPajakInfo(
        @Url url: String = "https://info-plat.ajisetiawan883.workers.dev/api/plat/check",
        @Body request: UniversalPajakRequest
    ): Response<JabarPajakResponse>
}
