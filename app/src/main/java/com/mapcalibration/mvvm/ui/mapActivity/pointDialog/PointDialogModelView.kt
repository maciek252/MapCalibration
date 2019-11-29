package com.mapcalibration.mvvm.ui.mapActivity.pointDialog

//import androidx.lifecycle.MutableLiveData

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mapcalibration.mvvm.data.db.entity.Point
import com.mapcalibration.mvvm.ui.mapActivity.MapViewModel


class PointDialogModelView(val mapViewModel: MapViewModel) : ViewModel() {



    var punktyOsnowy = MutableLiveData<List<Point>>()

    var _punkt = MutableLiveData<Point>()
    var punkt: MutableLiveData<Point>
        get() = _punkt
        set(value){
            _punkt = value
        }

    init{
        punktyOsnowy.value = mapViewModel.points.value
    }


    private val colorNoData = MutableLiveData<Int>()

//    val colorIdData = colorNoData.map {
//        when(it) {
//            0 -> R.color.colorPrimary
//            1 -> R.color.colorPrimaryDark
//            else -> R.color.colorAccent
//        }
//    }
//
//    val colorNameId = colorNoData.map {
//        when(it) {
//            0 -> R.string.color_primary
//            1 -> R.string.color_primary_dark
//            else -> R.string.color_accent
//        }
//    }
//
//    fun setColorNo(colorNo: Int) {
//        this.colorNoData.value = colorNo
//    }

}