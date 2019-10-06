package com.leopold.mvvm.ui.mackowaActivity

//import androidx.lifecycle.MutableLiveData

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.leopold.mvvm.data.db.entity.Point


class PointDialogModelView(val mackowaViewModel: MackowaViewModel) : ViewModel() {



    var punktyOsnowy = MutableLiveData<List<Point>>()

    var _punkt = MutableLiveData<Point>()
    var punkt: MutableLiveData<Point>
        get() = _punkt
        set(value){
            _punkt = value
        }

    init{
        punktyOsnowy.value = mackowaViewModel.points.value
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