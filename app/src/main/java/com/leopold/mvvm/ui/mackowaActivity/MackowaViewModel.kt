package com.leopold.mvvm.ui.mackowaActivity


import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.google.android.gms.common.util.MapUtils

import com.google.android.gms.maps.model.LatLng
import com.leopold.mvvm.core.BaseViewModel
import com.leopold.mvvm.data.db.dao.PointDao


import com.leopold.mvvm.data.db.entity.Bookmark
import com.leopold.mvvm.data.db.entity.Point
import com.leopold.mvvm.data.remote.api.SearchAPI
import com.leopold.mvvm.data.remote.domain.Repository
import com.leopold.mvvm.extension.with

import com.leopold.mvvm.util.*
import com.patloew.rxlocation.RxLocation
import kotlinx.coroutines.*
import android.widget.CompoundButton



/**
 * @author Leopold
 */
class MackowaViewModel(
    private val dao: PointDao
    ) : BaseViewModel() {

    private var query: String = ""
        get() = if (field.isEmpty()) "MVVM" else field

    var radio_checked = MutableLiveData<Int>()

    val centerMapOnGps: MutableLiveData<Boolean> = MutableLiveData()

    val heading: MutableLiveData<Double> = MutableLiveData()
    val distance: MutableLiveData<Double> = MutableLiveData()

    val currentGpsPosition : MutableLiveData<Location> = MutableLiveData()

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

    fun getAllSavedCollections(): List<String> {
        val defScope = CoroutineScope(Dispatchers.Default)
        var result = setOf("")
        runBlocking(defScope.coroutineContext) {
            val m= dao.findAllPoints()
            m.toList().map{result += it.collection}
        }
        //defScope.async{}

        //val job = defScope.async{

        //job.getCompleted()

        return result.toList()

    }

    fun saveAllPoints(collectioName: String) : Job {
        val defScope = CoroutineScope(Dispatchers.Default)
        return defScope.launch {
          points.value.map {
              it.collection = collectioName
              dao.insert(it)
          }
        }
    }



    fun readAllPoints(collectionName: String) : Job {
        val defScope = CoroutineScope(Dispatchers.Default)
        return defScope.launch {

            //val items: LiveData<PagedList<Point>> = LivePagedListBuilder(dao.findAllPoints(),  /* page size */ 10).build()

//val mm = items.value
             val m= dao.findAllPoints()
            Log.d("koko", "m=" + m.size + "/" + m.toString())
                //m.toList().map{points.value += it}
  //          dao.findAllPoints().map { points.value += it }
            points.postValue(m.toList().filter{it.collection == collectionName})
        }

    }



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

    fun onCheckedChange(button: CompoundButton, check: Boolean) {
        Log.d("Z1D1", "onCheckedChange: $check")
    }


    init{

        //centerMapOnGps.value = false
        points.observeForever {
            Log.d("mackowaVM", "setting points")
            computeScaleIfAvailable()
            computeTargets()
        }
        scaleTerrainMetersToMapCm.value = -1.0

        currentGpsPosition.observeForever {
            if(currentPoint.value != null && it != null){

                val pair =  computeDistanceAndHeadingToCurrentPoint(
                    PointToLocation(currentPoint.value!!), it!!)
                distance.value = pair.first
                heading.value = pair.second

            } else {
                distance.value = -1.0
                heading.value = -1.0
            }
        }

        radio_checked.observeForever {
            Log.d("mackowaVM", "radio")
        }

    }





    fun computeTargets(){


        points.value.filter{it.pointType == Point.PointType.ZWYKLY_XY}.map {

            //val p0 = points.value.get(0)
            val id = it.referenceId
            val p0 = points.value.filter{it.id == id}.get(0)
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

    fun removePoint(p: Point?){

        p?.let {
            points.value -= p
        }

    }



public fun onClickedCheckBox(){
Log.d("klik", "klik")
}



    fun onQueryChange(query: CharSequence) {
        this.query = query.toString()
    }

    //fun saveToBookmark(repository: Repository) = ioThread { dao.insert(Bookmark.to(repository)) }

    
}