package com.mapcalibration.mvvm

import com.mapcalibration.mvvm.data.remote.api.SearchAPI
import org.koin.dsl.module.module

/**
 * @author Maciej Szreter
 */

val testApiModule = module {
    single { DummySearchAPI() as SearchAPI }
}