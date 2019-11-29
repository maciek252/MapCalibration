package com.mapcalibration.mvvm.extension

import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

/**
 * @author Maciej Szreter
 */

@BindingAdapter("refreshing")
fun SwipeRefreshLayout.refreshing(visible: Boolean) {
    isRefreshing = visible
}