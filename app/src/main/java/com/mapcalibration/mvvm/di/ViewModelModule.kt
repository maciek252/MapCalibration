package com.mapcalibration.mvvm.di


import com.mapcalibration.mvvm.ui.mapActivity.MapViewModel

import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

/**
 * @author Maciej Szreter
 */
val viewModelModule = module {
    //viewModel { SearchViewModel(get(), get()) }
    //viewModel { BookmarkViewModel(get()) }
    viewModel { MapViewModel(get()) }
    //viewModel { MackowaViewModel() }
}
