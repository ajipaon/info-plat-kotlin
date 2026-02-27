package com.paondev.infoplat

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class InfoPlatApplication : Application() {
    companion object {
        lateinit var instance: InfoPlatApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}
