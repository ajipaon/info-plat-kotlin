package com.paondev.infoplat.di

import com.paondev.infoplat.InfoPlatApplication
import com.paondev.infoplat.data.api.InfoPlatApi
import com.paondev.infoplat.data.api.RetrofitClient
import com.paondev.infoplat.data.locale.AppDatabase
import com.paondev.infoplat.data.locale.InfoPlatDao
import com.paondev.infoplat.data.repository.ProvinceRepository
import com.paondev.infoplat.ui.viewmodel.ProvinceViewModel
import com.paondev.infoplat.ui.viewmodel.SearchHistoryViewModel
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
    fun provideAppDatabase(): AppDatabase {
        return AppDatabase.getDatabase(InfoPlatApplication.instance)
    }

    @Provides
    @Singleton
    fun provideInfoPlatDao(database: AppDatabase): InfoPlatDao {
        return database.infoPlatDao()
    }

    @Provides
    @Singleton
    fun provideProvinceRepository(api: InfoPlatApi, dao: InfoPlatDao): ProvinceRepository {
        return ProvinceRepository(api, dao)
    }
}
