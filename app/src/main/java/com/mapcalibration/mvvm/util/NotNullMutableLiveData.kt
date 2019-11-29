package com.mapcalibration.mvvm.util

import androidx.lifecycle.MutableLiveData

/**
 * @author Maciej Szreter
 */
class NotNullMutableLiveData<T : Any>(defaultValue: T) : MutableLiveData<T>() {

    init {
        value = defaultValue
    }

    override fun getValue()  = super.getValue()!!
}