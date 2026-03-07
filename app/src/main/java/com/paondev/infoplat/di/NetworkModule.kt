package com.paondev.infoplat.di

import android.content.Context
import com.paondev.infoplat.InfoPlatApplication
import com.paondev.infoplat.config.DataStoreManager
import com.paondev.infoplat.data.api.InfoPlatApi
import com.paondev.infoplat.data.api.RetrofitClient
import com.paondev.infoplat.data.locale.AppDatabase
import com.paondev.infoplat.data.locale.InfoPlatDao
import com.paondev.infoplat.data.repository.ProvinceRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    @MainApi
    fun provideInfoPlatApi(): InfoPlatApi {
        return RetrofitClient.apiService
    }

    @Provides
    @Singleton
    @com.paondev.infoplat.di.JatimApi
    fun provideJatimApi(): InfoPlatApi {
        return RetrofitClient.jatimApiService
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
    fun provideProvinceRepository(
        @MainApi api: InfoPlatApi,
        @com.paondev.infoplat.di.JatimApi jatimApi: InfoPlatApi,
        dao: InfoPlatDao
    ): ProvinceRepository {
        return ProvinceRepository(api, dao, jatimApi)
    }

    @Provides
    @Singleton
    fun provideDataStoreManager(@ApplicationContext context: Context): DataStoreManager {
        return DataStoreManager(context)
    }
}
