package com.mapcalibration.mvvm.di


import com.mapcalibration.mvvm.data.db.AppDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module.module

/**
 * @author Maciej Szreter
 */
val roomPointModule = module {
    single { AppDatabase.getInstance(androidApplication()) }
    single(createOnStart = false) { get<AppDatabase>().getPointDao() }
}