package com.mapcalibration.mvvm.core

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * @author Maciej Szreter
 * Kolon Corp
 * joongwon_baik@kolon.com
 */
open class BaseViewModel : ViewModel() {
    private val disposables: CompositeDisposable = CompositeDisposable()

    fun addToDisposable(disposable: Disposable) {
        disposables.add(disposable)
    }

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }
}