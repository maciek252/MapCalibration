package com.mapcalibration.mvvm

import android.app.Application
import android.content.Context
import android.util.Log
import com.mapcalibration.mvvm.di.*
import com.mapcalibration.mvvm.util.Configuration
import com.mapcalibration.mvvm.util.LocaleManager
import org.koin.android.ext.android.startKoin


/**
 * @author Maciej Szreter
 */
@Suppress("Unused")
class MVVMApp : Application() {

    override fun onCreate() {
        super.onCreate()

        Configuration.init(this)

        startKoin(this, listOf(
            viewModelModule,
            roomPointModule
        ))
    }

    companion object{
        var localeManager: LocaleManager? = null
    }


    val TAG = "main app"

    override fun attachBaseContext(base: Context) {
        localeManager = LocaleManager(base)
        super.attachBaseContext(localeManager?.setLocale(base))
        Log.d(TAG, "attachBaseContext")
    }

    override fun onConfigurationChanged(newConfig: android.content.res.Configuration) {
        super.onConfigurationChanged(newConfig)
        localeManager?.setLocale(this)
        //Log.d(TAG, "onConfigurationChanged: " + newConfig.locale.language)
    }
}