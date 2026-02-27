package com.paondev.infoplat.di

import com.paondev.infoplat.data.api.InfoPlatApi
import com.paondev.infoplat.data.api.RetrofitClient
import com.paondev.infoplat.data.repository.ProvinceRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideInfoPlatApi(): InfoPlatApi {
        return RetrofitClient.apiService
    }

    @Provides
    @Singleton
    fun provideProvinceRepository(api: InfoPlatApi): ProvinceRepository {
        return ProvinceRepository(api)
    }
}