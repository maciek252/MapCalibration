package com.leopold.mvvm.ui.MackowaActivity

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View

import androidx.annotation.LayoutRes

import com.leopold.mvvm.R
import com.leopold.mvvm.ui.BindingActivity

import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*

import com.leopold.mvvm.databinding.ActivityMackowyBinding
import com.leopold.mvvm.ui.mackowaActivity.mackowaActivity_pointDialog.PointDialog
import kotlinx.android.synthetic.main.activity_mackowy.*
import org.koin.androidx.viewmodel.ext.android.getViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.LatLng
import android.widget.Toast
import androidx.lifecycle.Observer
import com.leopold.mvvm.data.db.entity.Point
import com.leopold.mvvm.util.Utils
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.ui.IconGenerator
import com.google.android.gms.location.LocationRequest
import com.jakewharton.rxbinding2.view.clicks
import com.leopold.mvvm.ui.mackowaActivity.ConfigurationDialog
import com.leopold.mvvm.util.Configuration
import com.patloew.rxlocation.RxLocation
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit


class MackowaActivity : BindingActivity<ActivityMackowyBinding>(), OnMapReadyCallback, GoogleMap.OnMapLoadedCallback,
    GoogleMap.OnMarkerClickListener, GoogleMap.OnMarkerDragListener {



    var centeredOnInit: Boolean = false
    var mapLoaded: Boolean = false

    @LayoutRes
    override fun getLayoutResId() = R.layout.activity_mackowy

    var markerAddedByClick : Marker? = null

    val mapMarkerPoint: MutableMap<Marker,Point> = mutableMapOf()

    val TAG = "MackowyActivity"

    private lateinit var googleMap: GoogleMap

    lateinit var utils: Utils
    //lateinit var utils: Utils

    var job : Job? = null

    fun startTimeout() {
        val uiScope = CoroutineScope(Dispatchers.Main)
        job = uiScope.launch{
            delay(2000)
            binding.textViewHeadingToCurrent.setText("---")
            binding.textViewMapDistanceToCurrent.setText("---")

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        utils = Utils(applicationContext)

        binding.vm = getViewModel()
        binding.setLifecycleOwner(this)

        (binding.vm?.centerMapOnGps)?.value = Configuration.isMapCentered

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        buttonV.setOnClickListener {
            Log.d(TAG, "wcisniety")
            if(viewForHolder.visibility == View.VISIBLE){
                viewForHolder.visibility = View.GONE
            } else
                viewForHolder.visibility = View.VISIBLE
        }

        buttonAddPoint.setOnClickListener {
            showAddPointDialog()
        }
        buttonShowAllMarkers.setOnClickListener {
            showMarkers((binding.vm?.points)?.value!!)
        }
        //.
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



//        buttonSettings.setOnClickListener {
//            //binding.vm?.saveAllPoints("aktualna")
//            centerMapOnGpsLocation()
//        }
        buttonMenu.setOnClickListener {
            showConfigurationDialog()
            //binding.vm?.readAllPoints()
//            applicationContext.deleteDatabase("mvvm_demo.db")
        }



        binding.vm?.currentGpsPosition?.observeForever {

            job?.cancel("ee")
            startTimeout()
            if(binding.vm?.currentPoint?.value == null) {
                binding.textViewHeadingToCurrent.setText("====")
                binding.textViewMapDistanceToCurrent.setText("====")
            }
            else {
                val h = String.format("%.2f", binding.vm?.heading?.value)
                binding.textViewHeadingToCurrent.setText("" + h)
                val d = String.format("%.2f", binding.vm?.distance?.value)
                binding.textViewMapDistanceToCurrent.setText("" + d)
            }
        }

        binding.vm?.scaleTerrainMetersToMapCm?.observeForever {
            binding.textViewMapScaleCmToMeters.setText(String.format("%.0f", it))
            binding.textViewMapScaleMetersToCm.setText(String.format("%.3f", 100/it))


        }

        binding.vm?.currentPoint?.observeForever {


            val p = it
            //binding.vm?.currentPoint?.value
            binding.textViewCurrentPoint.setText("" + binding.vm?.currentPoint?.value)

            mapMarkerPoint.filter{ (key,value) ->value.id == p?.id}.map{(key,value)-> zoomToMarker(key)}



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
            .map{rxLocation -> textViewGps.setText("" + rxLocation.latitude);


                binding?.vm?.currentGpsPosition?.value = rxLocation


                //if(rxLocation.hasAccuracy() && rxLocation.accuracy < 2) {//&& !centeredOnInit){
                    centeredOnInit = true

                    if(binding?.vm?.centerMapOnGps?.value!!)
                        centerMapOnGpsLocation()
            }
            .subscribe{rxLocation->
                ;
            }
//            .flatMap<Address> { location ->
//                rxLocation.geocoding().fromLocation(location).toObservable()
//            }
//            .subscribe { address ->
//                /* do something */
//            }

    }



    override fun onMapReady(googleMap: GoogleMap) {
        Log.d(TAG, "onMapReady")
        Log.d(TAG, "onMapReady")
        this.googleMap = googleMap
        this.googleMap.setOnMapLoadedCallback(this)
        this.googleMap.setOnMarkerClickListener(this)


        this.googleMap.setOnMapClickListener { latLng ->
            // Creating a marker

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
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))

            // Placing a marker on the touched position
            markerAddedByClick = googleMap.addMarker(markerOptions)

            binding?.vm?.latLngMarker?.value = markerAddedByClick?.position


        }
        //googleMap.setMyLocationEnabled(true);
        googleMap.isMyLocationEnabled = true

    }

    fun centerMapOnGpsLocation(){


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
        val mapSettings = googleMap?.uiSettings
        mapSettings?.isZoomControlsEnabled = true
        googleMap.mapType = GoogleMap.MAP_TYPE_HYBRID

//        binding.vm?.points2?.observe(this, ResourceObserver("MackowaActivity",
//            hideLoading = ::none,
//            showLoading = ::none,
//            onSuccess = ::showMarkers,
//            onError = ::showErrorMessage)
//        )



        binding.vm?.points?.observe(this, Observer{
                //points_recycler_view.notify
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
            Point.PointType.ZWYKLY_DWIE_LINIE -> BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)
            Point.PointType.ZWYKLY_XY -> BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
            else -> BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)
        }

        val icon = when (p.len1Ref) {
            1 -> BitmapDescriptorFactory.fromBitmap(utils.getBitmap(R.drawable.ic_peru))
            2 -> BitmapDescriptorFactory.fromBitmap(utils.getBitmap(R.drawable.ic_italy))
            3 -> BitmapDescriptorFactory.fromBitmap(utils.getBitmap(R.drawable.ic_chile))
            else -> null
        }

        //snippet to distinguish markers within UiAutomator
        return MarkerOptions().position(point).title("" + p.id).snippet("" + p.id).icon(icon2)
            //.icon(icon)
    }


    private fun showErrorMessage(error: String) {
        Toast.makeText(this, "Error: $error", Toast.LENGTH_LONG).show()
    }

    private fun zoomToMarker(marker: Marker){

        val zoom = googleMap.getCameraPosition().zoom

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
        points.map {
            val marker =googleMap.addMarker(constructMarkerOptions(it))

            mapMarkerPoint[marker] = it

            val iconColor = when(it.pointType){
                Point.PointType.OSNOWA_COORDINATES -> IconGenerator.STYLE_RED
                Point.PointType.OSNOWA_MARKER_XY -> IconGenerator.STYLE_ORANGE
                Point.PointType.ZWYKLY_DWIE_LINIE -> IconGenerator.STYLE_BLUE
                Point.PointType.ZWYKLY_XY -> IconGenerator.STYLE_GREEN
                else -> IconGenerator.STYLE_WHITE
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
        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 45))
    }




    override fun onMarkerDrag(p0: Marker?) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onMarkerDragEnd(p0: Marker?) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onMarkerDragStart(p0: Marker?) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun onMarkerClick(marker: Marker): Boolean {

        val pos = mapMarkerPoint.get(marker)?.id
        pos?.let {
            points_recycler_view?.scrollToPosition(pos!!)
            Log.d("T", "skroluje do pkt: ${pos}")
        }

        binding?.vm?.latLngMarker?.value = marker.position

        zoomToMarker(marker)



        return true
    }


    fun showConfigurationDialog(){

        val d = ConfigurationDialog(binding.vm!!)
        //d.binding?.item
        val s: String = "configuration"
        d?.show(supportFragmentManager, s)
    }

    fun showAddPointDialog(){
        val vm = (binding.vm)
        val d =
            PointDialog(vm!!, null)

        val s: String = "dialog"
        d?.show(supportFragmentManager, s)
    }
}
