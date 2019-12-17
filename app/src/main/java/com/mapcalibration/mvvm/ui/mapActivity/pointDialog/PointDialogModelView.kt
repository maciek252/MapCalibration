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



}