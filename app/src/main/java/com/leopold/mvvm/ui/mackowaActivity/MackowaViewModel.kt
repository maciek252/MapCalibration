package com.leopold.mvvm.ui.mackowaActivity


import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.google.android.gms.common.util.MapUtils

import com.google.android.gms.maps.model.LatLng
import com.leopold.mvvm.core.BaseViewModel
import com.leopold.mvvm.data.db.dao.BookmarkDao
import com.leopold.mvvm.data.db.entity.Bookmark
import com.leopold.mvvm.data.db.entity.Point
import com.leopold.mvvm.data.remote.api.SearchAPI
import com.leopold.mvvm.data.remote.domain.Repository
import com.leopold.mvvm.extension.with

import com.leopold.mvvm.util.*

/**
 * @author Leopold
 */
class MackowaViewModel() : BaseViewModel() {
    private var query: String = ""
        get() = if (field.isEmpty()) "MVVM" else field


    val currentPoint: MutableLiveData<Point?> = MutableLiveData()

    val scaleTerrainMetersToMapCm: MutableLiveData<Double> = MutableLiveData()


    val latLngMarker: MutableLiveData<LatLng> = MutableLiveData()

    val liczba: NotNullMutableLiveData<Int> = NotNullMutableLiveData(1)

    private val _refreshing: NotNullMutableLiveData<Boolean> = NotNullMutableLiveData(false)
    val refreshing: NotNullMutableLiveData<Boolean>
        get() = _refreshing

    private val _itemsRyba: NotNullMutableLiveData<List<Repository>> = NotNullMutableLiveData(arrayListOf())
    val items: NotNullMutableLiveData<List<Repository>>
        get() = _itemsRyba



//    private val _points: NotNullMutableLiveData<List<Point>> = NotNullMutableLiveData(arrayListOf())
    var points: NotNullMutableLiveData<List<Point>> = NotNullMutableLiveData(listOf())
//        get() = points
//        set(v) {
//            Log.d("mackowaVM", "setting points")
//            field = v
//        }
  //      get() = _points

//    val points2: MutableLiveData<Resource<List<Point>>> = MutableLiveData()
//      //  Transformations.map(punkty){it}


    init{
        points.observeForever {
            Log.d("mackowaVM", "setting points")
            computeScaleIfAvailable()
            computeTargets()
        }
        scaleTerrainMetersToMapCm.value = -1.0
    }



    fun computeTargets(){


        points.value.filter{it.pointType == Point.PointType.ZWYKLY_XY}.map {

            val p0 = points.value.get(0)
            computeTarget(it, p0, scaleTerrainMetersToMapCm.value!!)
        }
    }

    fun computeScaleIfAvailable(){
        val p1: Point? = points.value.filter{it.pointType == Point.PointType.OSNOWA_COORDINATES}.firstOrNull()
        val p2: Point? = points.value.filter{it.pointType == Point.PointType.OSNOWA_MARKER_XY}.firstOrNull()


        p1?.let{p2?.let{
            val l1: Location = Location("p1")
            l1.latitude = p1.latitude
            l1.longitude = p1.longitude
            val l2: Location = Location("p2")
            l2.latitude = p2.latitude
            l2.longitude = p2.longitude
            scaleTerrainMetersToMapCm.value = computeDistance(l1,l2, p2.x, p2.y )}}

    }

    fun doSearchC(s: CharSequence?) {

        val m: String = s.toString()
        val params3 = mapOf("q" to m, "sort" to "stars")


    }

    fun refreshPoints(){
        val m = points.value
        points.value = m

    }

    fun doSearchS(s: String) {

    }



    fun onQueryChange(query: CharSequence) {
        this.query = query.toString()
    }

    //fun saveToBookmark(repository: Repository) = ioThread { dao.insert(Bookmark.to(repository)) }

    
}