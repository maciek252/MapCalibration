package com.mapcalibration.mvvm.ui.mapActivity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle

import android.util.Log
import android.view.View

import androidx.annotation.LayoutRes


import com.mapcalibration.mvvm.ui.BindingActivity

import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*

import com.mapcalibration.mvvm.databinding.ActivityMapBinding
import com.mapcalibration.mvvm.ui.mapActivity.pointDialog.PointDialog
import kotlinx.android.synthetic.main.activity_map.*
import org.koin.androidx.viewmodel.ext.android.getViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.LatLng
import android.widget.Toast

import androidx.lifecycle.Observer
import com.mapcalibration.mvvm.data.db.entity.Point
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.ui.IconGenerator
import com.google.android.gms.location.LocationRequest

import com.mapcalibration.mvvm.MVVMApp
import com.mapcalibration.mvvm.ui.configurationDialog.ConfigurationDialog
import com.mapcalibration.mvvm.util.Configuration
import com.patloew.rxlocation.RxLocation

import kotlinx.coroutines.*

import androidx.core.app.ActivityCompat
import com.mapcalibration.mvvm.R
import com.mapcalibration.mvvm.util.Utility


class MapActivity : BindingActivity<ActivityMapBinding>(), OnMapReadyCallback, GoogleMap.OnMapLoadedCallback,
    GoogleMap.OnMarkerClickListener, GoogleMap.OnMarkerDragListener {

/*

    private val INITIAL_PERMS =
        arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_CONTACTS)
    private val CAMERA_PERMS = arrayOf<String>(Manifest.permission.CAMERA)
    private val CONTACTS_PERMS = arrayOf<String>(Manifest.permission.READ_CONTACTS)
    private val LOCATION_PERMS = arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION)
    private val INITIAL_REQUEST = 1337
*/

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(MVVMApp.localeManager?.setLocale(base))
        Log.d(TAG, "attachBaseContext")
    }

    private var centeredOnInit: Boolean = false


    @LayoutRes
    override fun getLayoutResId() = R.layout.activity_map


    private var markerAddedByClick : Marker? = null

    private val mapMarkerPoint: MutableMap<Marker,Point> = mutableMapOf()

    private val TAG = "MapActivity"

    private lateinit var googleMap: GoogleMap


    private var job : Job? = null

    private fun startTimeout() {
        val uiScope = CoroutineScope(Dispatchers.Main)
        job = uiScope.launch{
            delay(2000)
            binding.textViewHeadingToCurrent.text = "---"
            binding.textViewMapDistanceToCurrent.text = "---"

        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        //ActivityCompat.requestPermissions(INITIAL_PERMS, INITIAL_REQUEST)
        //ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        val recordRequestCode = 101
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            recordRequestCode
        )




        binding.vm = getViewModel()
        //binding.setLifecycleOwner(this)
        binding.lifecycleOwner = this

        (binding.vm)?.centerMapOnGps?.value = Configuration.isMapCentered

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        imageButton?.setOnClickListener {

            //setNewLocale(LANGUAGE_POLISH, false)

            Log.d(TAG, "pressed")
            if (viewForHolder.visibility == View.VISIBLE) {
                viewForHolder.visibility = View.GONE
                //buttonV?.setImageRe
                imageButton?.setImageResource(android.R.drawable.arrow_down_float)

                //Utility.
            } else {
                viewForHolder.visibility = View.VISIBLE
                imageButton?.setImageResource(android.R.drawable.arrow_up_float)

            }
        }

        buttonAddPoint.setOnClickListener {
            showAddPointDialog()
        }
        buttonShowAllMarkers.setOnClickListener {

            if (binding.vm?.points?.value!!.isNotEmpty()) {

                //applyLanguage(this, "en")
                //setNewLocale(LANGUAGE_ENGLISH, false)
                showMarkers(binding.vm?.points?.value!!)
                binding.vm?.latLngMarker?.value.let {
                    if(it != null)
                        addMarkerPin(it!!, false)

                }


            }
        }
        //.

/*
        val clickStream = buttonSettings
            .clicks().share()

        //    .debounce(300, TimeUnit.MILLISECONDS)
//            .observeOn(AndroidSchedulers.mainThread())
            //.buffer(1000, TimeUnit.MILLISECONDS)
            //.filter(clicks -> clicks.size() == 4)
    //        .count()
//            .filter{count -> count >= 1}
            //.map{count -> count >= 1}
  //          .observeOn(AndroidSchedulers.mainThread())
            clickStream.buffer(clickStream.debounce(250, TimeUnit.MILLISECONDS))
            .map { it.size }
            .filter { it == 2 }
                .observeOn(AndroidSchedulers.mainThread())
            .subscribe{ Log.d(TAG, "double click!")
            buttonSettings.isPressed = !buttonSettings.isPressed
                        centerMapOnGpsLocation()
        }
*/


//        buttonSettings.setOnClickListener {
//            //binding.vm.saveAllPoints("aktualna")
//            centerMapOnGpsLocation()
//        }
        buttonMenu.setOnClickListener {
            showConfigurationDialog()
            //binding.vm.readAllPoints()
//            applicationContext.deleteDatabase("mvvm_demo.db")
        }


        binding.vm?.keepScreenOn?.observeForever {

            Utility.setScreenStayOn(binding.vm?.keepScreenOn?.value!!, this)

        }

        binding.vm?.keepScreenOn?.value = Configuration.keepScreenOn


        binding.vm?.currentGpsPosition?.observeForever {

            job?.cancel("ee")
            startTimeout()
            if (binding.vm?.currentPoint?.value == null) {
                binding.textViewHeadingToCurrent.text = "=="
                binding.textViewMapDistanceToCurrent.text = "=="
            } else {
                val h = String.format("%.0f", binding.vm?.heading?.value)
                binding.textViewHeadingToCurrent.text = h
                val d = String.format("%.0f", binding.vm?.distance?.value)
                binding.textViewMapDistanceToCurrent.text = d
            }
        }

        binding.vm?.scaleTerrainMetersToMapCm?.observeForever {
            if (it == -1.0) {
                binding.textViewMapScaleCmToMeters.text = "--"
                binding.textViewMapScaleMetersToCm.text = "--"
            } else {
                binding.textViewMapScaleCmToMeters.text = String.format("%.2f", it)
                binding.textViewMapScaleMetersToCm.text = String.format("%.2f", 100 / it)
            }


        }

        binding.vm?.focusMapOnPoint?.observeForever {
            val p = it
            p.let {
                mapMarkerPoint.filter { (_, value) -> value.id == p?.id }
                    .map { (key, _) -> zoomToMarker(key) }
            }
        }

        binding.vm?.currentPoint?.observeForever {

            val p = it
            //binding.vm.currentPoint?.value
            if (binding.textViewCurrentPoint == null)
                binding.textViewCurrentPoint.text = "---"
            else
                binding.textViewCurrentPoint.text = "" + binding.vm?.currentPoint?.value?.name

            mapMarkerPoint.filter { (_, value) -> value.id == p?.id }
                .map { (key, _) -> zoomToMarker(key) }


        }

        binding.vm?.points?.observeForever {


            binding.pointsRecyclerView.adapter?.notifyDataSetChanged()
        }


        // Create one instance and share it
        val rxLocation = RxLocation(this.applicationContext)

        val locationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(1000)




        rxLocation.location().updates(locationRequest)
            .map { rxLocationL ->
                // textViewGps.setText("" + rxLocation.latitude);


                binding.vm?.currentGpsPosition?.value = rxLocationL


                //if(rxLocation.hasAccuracy() && rxLocation.accuracy < 2) {//&& !centeredOnInit){
                centeredOnInit = true

                if (binding.vm?.centerMapOnGps?.value!!)
                    centerMapOnGpsLocation()
            }
            .subscribe { _ ->
                ;
            }
//            .flatMap<Address> { location ->
//                rxLocation.geocoding().fromLocation(location).toObservable()
//            }
//            .subscribe { address ->
//                /* do something */
//            }

        binding.vm?.latLngMarker.let {
            it?.observeForever {
                showMarkerPin()
            }
        }


    }




    override fun onMapReady(googleMap: GoogleMap) {
        Log.d(TAG, "onMapReady")
        this.googleMap = googleMap
        this.googleMap.setOnMapLoadedCallback(this)
        this.googleMap.setOnMarkerClickListener(this)


        this.googleMap.setOnMapClickListener { latLng ->
            // Creating a marker


            addMarkerPin(latLng)

        }
        //googleMap.setMyLocationEnabled(true);
        googleMap.isMyLocationEnabled = true

    }

    private fun showMarkerPin(){

        if(markerAddedByClick == null || !markerAddedByClick?.isVisible!!){
            //addMarkerPin()
            addMarkerPin(binding.vm?.latLngMarker?.value!!)
        }
    }

    private fun addMarkerPin(latLng: LatLng, center: Boolean = true){
        Log.d(TAG, "setting Marker Pin")

        markerAddedByClick?.remove()

        val markerOptions = MarkerOptions()

        // Setting the position for the marker
        markerOptions.position(latLng)

        // Setting the title for the marker.
        // This will be displayed on taping the marker
        markerOptions.title(latLng.latitude.toString() + " : " + latLng.longitude)

        // Clears the previously touched position
        //googleMap.clear()

        // Animating to the touched position
        if(center)
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))

        // Placing a marker on the touched position
        markerAddedByClick = googleMap.addMarker(markerOptions)

        this.binding.vm?.latLngMarker?.value = markerAddedByClick?.position

    }

    private fun centerMapOnGpsLocation(){


        val center = CameraUpdateFactory.newLatLng(
            LatLng(
                binding.vm?.currentGpsPosition?.value?.latitude!!,
                binding.vm?.currentGpsPosition?.value?.longitude!!

            )
        )
        val zoom = CameraUpdateFactory.zoomTo(15f)

        googleMap.moveCamera(center)
        googleMap.animateCamera(zoom)
    }


    override fun onMapLoaded() {
        Log.d(TAG, "onMapLoadedCallback")
        val mapSettings = googleMap.uiSettings
        mapSettings?.isZoomControlsEnabled = true
        googleMap.mapType = GoogleMap.MAP_TYPE_HYBRID

//        binding.vm.points2?.observe(this, ResourceObserver("MackowaActivity",
//            hideLoading = ::none,
//            showLoading = ::none,
//            onSuccess = ::showMarkers,
//            onError = ::showErrorMessage)
//        )



        binding.vm?.points?.observe(this, Observer{
                //points_recycler_view?.invalidate()
            //points_recycler_view?.setOffs
              //  points_recycler_view?.adapter?.setOffsetOffscreenPageLimi
                points_recycler_view?.adapter?.notifyDataSetChanged()
                showMarkers((binding.vm?.points)?.value!!)
            }
        )


    }




    private fun constructMarkerOptions(p: Point): MarkerOptions {
        val point = LatLng(p.latitude, p.longitude)

        // to niewazne! nie wykorzystane
        val icon2 = when(p.pointType){
            Point.PointType.OSNOWA_COORDINATES -> BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
            Point.PointType.OSNOWA_MARKER_XY -> BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)
            Point.PointType.TARGET_TWO_DISTANCES -> BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)
            Point.PointType.TARGET_XY -> BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
        }



        //snippet to distinguish markers within UiAutomator
        return MarkerOptions().position(point).title("" + p.id).snippet("" + p.id).icon(icon2)
            //.icon(icon)
    }


    private fun zoomToMarker(marker: Marker){

        val zoom = googleMap.cameraPosition.zoom


        //Build camera position
        val cameraPosition = CameraPosition.Builder()
            .target(marker.position)
            .zoom(zoom+1).build()
        //Zoom in and animate the camera.
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

    }

    private fun showMarkers(points: List<Point>) {

        mapMarkerPoint.clear()
        googleMap.clear()
        if(points.isEmpty())
            return




        val builder = LatLngBounds.Builder()
        points.filter{it.isValid}.map {

            val marker =googleMap.addMarker(constructMarkerOptions(it))

            mapMarkerPoint[marker] = it

            val iconColor = when(it.pointType){
                Point.PointType.OSNOWA_COORDINATES -> IconGenerator.STYLE_RED
                Point.PointType.OSNOWA_MARKER_XY -> IconGenerator.STYLE_ORANGE
                Point.PointType.TARGET_TWO_DISTANCES -> IconGenerator.STYLE_BLUE
                Point.PointType.TARGET_XY -> IconGenerator.STYLE_GREEN
            }

            val iconFactory = IconGenerator(this) //.setColor(STYLE_BLUE)
            iconFactory.setStyle(iconColor)
            marker.setIcon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(it.name)))
            //marker.showInfoWindow()


            Pair(it, marker)
        }.map {
            it.second.tag = it.first
            it.second
        }.map {
            builder.include(it.position)
        }
//qqq
        markerAddedByClick?.isVisible?.let{
            markerAddedByClick?.position?.let {
                builder.include(markerAddedByClick?.position)
            }
        }


        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 45))
    }




    override fun onMarkerDrag(p0: Marker?) {

    }

    override fun onMarkerDragEnd(p0: Marker?) {

    }

    override fun onMarkerDragStart(p0: Marker?) {

    }


    override fun onMarkerClick(marker: Marker): Boolean {

        //binding.pointsRecyclerView?.
        val pos = mapMarkerPoint[marker]?.id

        pos?.let {

//            for(i in 0..binding.vm.points?.value?.size!!){
//                if(binding.vm.points?.value?.get(i)?.id == pos){
//                    points_recycler_view?.scrollToPosition(i)
//                    break
//                }
//            }
            val i = binding.vm?.points?.value?.indexOfFirst { it.id == pos }
            i.let{
                //points_recycler_view?.scrollToPosition(i!!)
                points_recycler_view?.smoothScrollToPosition(i!!)
                //points_recycler_view?.invalidate()
                //points_recycler_view?.invalidateOutline()

            }
            Log.d("T", "skroluje do i: $i")



//
//            for( i in 0..points_recycler_view?.childCount!!){
//                val v = points_recycler_view?.getChildAt(i)
//                val vh = points_recycler_view?.getChildViewHolder(v!!)
//
//                //vh?.itemView?.text
//            }
            //
            //points_recycler_view?.scrollToPosition(pos!!)
            Log.d("T", "skroluje do pkt: $pos")
        }

        binding.vm?.latLngMarker?.value = marker.position

        zoomToMarker(marker)



        return true
    }


    private fun showConfigurationDialog(){

        val d = ConfigurationDialog(binding.vm!!)
        //d.binding.item
        val s = "configuration"
        d.show(supportFragmentManager, s)
    }

    fun showAddPointDialog(){
        val vm = (binding.vm)
        val d =
            PointDialog(vm!!, null)

        val s = "dialog"
        d.show(supportFragmentManager, s)
    }



    fun setNewLocale(language: String, restartProcess: Boolean): Boolean {
        MVVMApp.localeManager?.setNewLocale(this, language)

        val i = Intent(this, MapActivity::class.java)
        startActivity(i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))

        if (restartProcess) {
            System.exit(0)
        } else {
            Toast.makeText(this, "Activity restarted", Toast.LENGTH_SHORT).show()
        }
        return true
    }

}
