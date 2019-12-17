package com.mapcalibration.mvvm.ui.mapActivity


import android.location.Location
import android.util.Log
import androidx.lifecycle.MutableLiveData

import com.google.android.gms.maps.model.LatLng
import com.mapcalibration.mvvm.core.BaseViewModel
import com.mapcalibration.mvvm.data.db.dao.PointDao


import com.mapcalibration.mvvm.data.db.entity.Point

import com.mapcalibration.mvvm.util.*
import kotlinx.coroutines.*



/**
 * @author Maciej Szreter
 */
class MapViewModel(
    private val dao: PointDao
    ) : BaseViewModel() {

    private var query: String = ""
        get() = if (field.isEmpty()) "MVVM" else field

    val TAG = "MapViewModel"

    var radio_checked = MutableLiveData<Int>()

    val centerMapOnGps: MutableLiveData<Boolean> = MutableLiveData()

    val heading: MutableLiveData<Double> = MutableLiveData()
    val distance: MutableLiveData<Double> = MutableLiveData()

    val currentGpsPosition : MutableLiveData<Location> = MutableLiveData()

    val currentPoint: MutableLiveData<Point?> = MutableLiveData()
    val focusMapOnPoint: MutableLiveData<Point?> = MutableLiveData()

    val scaleTerrainMetersToMapCm: MutableLiveData<Double> = MutableLiveData()


    var keepScreenOn: MutableLiveData<Boolean> = MutableLiveData()

    var latLngMarker: MutableLiveData<LatLng> = MutableLiveData()





    fun removeAllPointsFromCollectionAndCollectionIfIsNotTheLastOne(){
        ioThread {
            dao.removeAllPointsFromCollection(Configuration.lastName)
        }
    }

    fun getAllSavedCollections(): List<String> {
        val defScope = CoroutineScope(Dispatchers.Default)
        var result : Set<String> = emptySet()
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
        ioThread {
            dao.removeAllPointsFromCollection(collectioName)
        }
        val defScope = CoroutineScope(Dispatchers.Default)
        return defScope.launch {
          points.value.map {
              it.collection = collectioName
              ioThread {
                  dao.insert(it)
              }
          }
        }
    }



    fun readAllPoints(collectionName: String) : Job {
        val defScope = CoroutineScope(Dispatchers.Default)
        return defScope.launch {

            //val items: LiveData<PagedList<Point>> = LivePagedListBuilder(dao.findAllPoints(),  /* page size */ 10).build()

//val mm = items.value
             val m= dao.findAllPoints()
            Log.d(TAG, "m=" + m.size + "/" + m.toString())
                //m.toList().map{points.value += it}
  //          dao.findAllPoints().map { points.value += it }
            points.postValue(m.toList().filter{it.collection == collectionName})
        }

    }



//    private val _points: NotNullMutableLiveData<List<Point>> = NotNullMutableLiveData(arrayListOf())
    var points: NotNullMutableLiveData<List<Point>> = NotNullMutableLiveData(listOf())
//        get() = points
//        set(v) {

//            field = v
//        }
  //      get() = _points

//    val points2: MutableLiveData<Resource<List<Point>>> = MutableLiveData()
//      //  Transformations.map(punkty){it}

    fun addPoint(p: Point){
        //points.value += p!!

        val m = points.value
        points.value += p
        points.value = m


        val colName = Configuration.lastName
        saveAllPoints(colName)
    }

    init{

        //centerMapOnGps.value = false
        points.observeForever {
            Log.d(TAG, "setting points")
            computeScaleIfAvailable()
            computeTargets()
        }
        scaleTerrainMetersToMapCm.value = -1.0

        currentGpsPosition.observeForever {
            if(currentPoint.value != null && it != null){

                val pair =  computeDistanceAndHeadingToCurrentPoint(
                    pointToLocation(currentPoint.value!!), it)
                distance.value = pair.first
                heading.value = pair.second

            } else {
                distance.value = -1.0
                heading.value = -1.0
            }
        }

        radio_checked.observeForever {
            Log.d(TAG, "radio")
        }

        val colName = Configuration.lastName
        readAllPoints(colName)

    }





    private fun computeTargets(){


        points.value.filter{it.pointType == Point.PointType.TARGET_XY}.map {
            it.isValid = true
            //val p0 = points.value.get(0)
            val id = it.referenceId
            val p0 = points.value.filter{it.id == id}.get(0)
            computeTargetXY(it, p0, scaleTerrainMetersToMapCm.value!!)
            if(it.latitude.isNaN() || it.longitude.isNaN()) {
                it.isValid = false
                it.longitude = NOT_VALID_LATLON
                it.latitude = NOT_VALID_LATLON
            }
        }

        points.value.filter{it.pointType == Point.PointType.TARGET_TWO_DISTANCES}.map {
            it.isValid = true
            //val p0 = points.value.get(0)
            val id = it.referenceId
            val id2 = it.referenceId2

            val p0 = points.value.filter{it.id == id}.get(0)
            val p1 = points.value.filter{it.id == id2}.get(0)
            computeTarget2Distances(it, p1, p0, scaleTerrainMetersToMapCm.value!!)
            if(it.latitude.isNaN() || it.longitude.isNaN()) {
                it.isValid = false
                it.longitude = NOT_VALID_LATLON
                it.latitude = NOT_VALID_LATLON
            }
        }
    }

    private fun computeScaleIfAvailable(){
        val p1: Point? = points.value.filter{it.pointType == Point.PointType.OSNOWA_COORDINATES}.firstOrNull()
        val p2: Point? = points.value.filter{it.pointType == Point.PointType.OSNOWA_MARKER_XY}.firstOrNull()


        p1?.let{p2?.let{
            val l1 = Location("p1")
            l1.latitude = p1.latitude
            l1.longitude = p1.longitude
            val l2 = Location("p2")
            l2.latitude = p2.latitude
            l2.longitude = p2.longitude
            scaleTerrainMetersToMapCm.value = computeDistance(l1,l2, p2.len1, 0.0 )}}

    }


    fun removePoint(p: Point?){

        var kopia = points.value

        p?.let {
            kopia -= p
        }
        var listToRemove = emptyList<Point?>()
        listToRemove += p

        while(!listToRemove.isEmpty()) {
            val pp = listToRemove.first()
            listToRemove -= pp
            Log.d("REMOVE", "want to remove p.id=" + pp?.id + "name=" +pp?.name)
            listToRemove += kopia.filter{it.len1Ref == pp?.id || it.len2Ref == pp?.id || it.referenceId == pp?.id || it.referenceId2 == pp?.id }
            Log.d("REMOVE", "listToRemove=" + listToRemove.toString())
            kopia -= pp!!


        }

        points.value = kopia

        val colName = Configuration.lastName
        saveAllPoints(colName)

    }





    
}