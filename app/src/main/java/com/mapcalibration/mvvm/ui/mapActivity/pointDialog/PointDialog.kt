package com.mapcalibration.mvvm.ui.mapActivity.pointDialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.gms.maps.model.LatLng
import com.mapcalibration.mvvm.R

import com.mapcalibration.mvvm.data.db.entity.Point
import com.mapcalibration.mvvm.databinding.PointEditDialogBinding
import com.mapcalibration.mvvm.ui.mapActivity.MapViewModel
import com.mapcalibration.mvvm.util.Utility


class PointDialog(val vm: MapViewModel, var p: Point?) : DialogFragment(),  AdapterView.OnItemSelectedListener,
    AdapterView.OnItemClickListener {
    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    private var pointDialogModelView =
        PointDialogModelView(vm)


    public var binding : PointEditDialogBinding? = null

    fun addPoint(){
        vm.points.value += p!!
        //vm.addPoint(p!!)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(com.mapcalibration.mvvm.R.layout.point_edit_dialog, container, false)
        binding =  DataBindingUtil.bind(v)!!

        var wasEmpty = false
        if(p == null){
            wasEmpty = true
            p = Point()
        }



        binding?.textViewPointName?.setText(p?.name)


        binding?.buttonRemovePoint?.setOnClickListener {
            vm.removePoint(p)
            this.dismiss()
        }

        binding?.buttonSavePoint?.setOnClickListener {

            if(p?.pointType == Point.PointType.OSNOWA_COORDINATES || p?.pointType == Point.PointType.OSNOWA_MARKER_XY)
                if(wasEmpty && vm?.latLngMarker.value == null){
                    Toast.makeText(dialog.context, getString(R.string.english3), Toast.LENGTH_LONG).show()
                    //Toast.makeText(dialog.context,"DD", Toast.LENGTH_LONG).show()
                    dismiss()
                    return@setOnClickListener
                } else if(vm?.latLngMarker.value == null){
                    Log.d("PointDialog", "osnowa point edited but marker empty, setting from coordinates")
                    val l = LatLng(p?.latitude!!, p?.longitude!!)
                    vm?.latLngMarker.value = l
                }

            p?.name = binding?.textViewPointName?.text.toString()

            val a = binding?.pagerView?.adapter as ColorPagerAdapter
            //val oo = a.getItem(binding?.pagerView?.currentItem!!)

            val isValid = a.m.get(binding?.pagerView?.currentItem!!)?.savePoint()
            //oo.savePoint()



            if(isValid!!) {


                if (wasEmpty)
                    addPoint()


                // to nie wystarcza - adapter nie wykonuje odswiezenia
                //binding?.vm?.points = binding?.vm?.points!!
                // DO POPRAWKI!!!! PASKUDNE!
//            val m = vm.points.value
//            vm.points.value += p!!
//            vm.points.value = m

                vm.addPoint(p!!)
            }

            this.dismiss()
        }

        pointDialogModelView._punkt.value = p

        initializePageViewer(v)
        initializeIndicator()



        if(wasEmpty){
            binding?.buttonRemovePoint?.isClickable = false
            binding?.buttonRemovePoint?.isEnabled = false

            if(vm.points.value.isEmpty()){

            } else
            if(vm.points.value.filter{it.pointType == Point.PointType.OSNOWA_MARKER_XY}.isEmpty()) {
                val tab =
                    binding?.indicatorLayout2?.getTabAt(Point.PointType.OSNOWA_MARKER_XY?.ordinal!!)
                tab?.select()
            } else  {
                val tab =
                    binding?.indicatorLayout2?.getTabAt(Point.PointType.TARGET_XY?.ordinal!!)
                tab?.select()
            }
        } else {
            val tab = binding?.indicatorLayout2?.getTabAt(p?.pointType?.ordinal!!)
            tab?.select()
        }



        return v
    }


    private fun initializePageViewer(v : View) {

        val s = childFragmentManager

        //val s = getActivity()?.getSupportFragmentManager()

        if(binding?.pagerView != null){
            Log.d("WW", "pagerView != null")
        }
        binding?.pagerView?.adapter = ColorPagerAdapter(s!!)
        //binding?.pagerView?.adapter?.get
        binding?.pagerView?.offscreenPageLimit = 2
        binding?.pagerView?.addOnPageChangeListener(pageChangeListener)


    }

    private fun initializeIndicator() {

        val listaTypow = Point.PointType.values().toList()
        binding?.indicatorLayout2?.setupWithViewPager(binding?.pagerView, true)
        for (tabNo in 0..listaTypow.size) {
            val a = binding?.indicatorLayout2?.getTabAt(tabNo)

            when(tabNo){
                0 -> a?.text = ""+ Utility.getStringFromResources(activity as AppCompatActivity, "marker")
                1 -> a?.text = ""+ Utility.getStringFromResources(activity as AppCompatActivity, "markerScale")
                2 -> a?.text = ""+ Utility.getStringFromResources(activity as AppCompatActivity, "target_xy")
                3 -> a?.text = ""+ Utility.getStringFromResources(activity as AppCompatActivity, "target_2lines")
            }

            //binding?.indicatorLayout2?.getTabAt(tabNo)?.text = ""+listaTypow.get(tabNo)
        }
    }

    private val pageChangeListener = object : ViewPager.SimpleOnPageChangeListener() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            //colorViewModel.setColorNo(position)
        }
    }

    private inner class ColorPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        val m = HashMap<Int,PointDialogFragment>()

        override fun getItem(position: Int) : PointDialogFragment {
            //if(position == 1)
                //return ColorFragment.putExtra(position).setModel(colorViewModel)
            //else if(position == 0)
//                return ColorFragmentMackowy.putExtraMackowy(position).setModel(colorViewModel)
//            return ColorFragmentMackowy.putExtraMackowy(position).setModel(colorViewModel)
            //return ColorFragment.putExtra(position).setModel(colorViewModel)
            //return ColorFragmentMackowy.putExtraMackowy(position).setModel(colorViewModel)
            if(position == 0) {
                val f =  PointDialogFragmentOsnowaCoordinates.putExtra(
                    position
                ).setModel(pointDialogModelView)
                f.setPoint(p!!)
                m[0] = f
                return f
            } else
            if(position == 1) {
                val f = PointDialogFragmentOsnowaXY.putExtra(
                    position
                ).setModel(pointDialogModelView)

                f.setPoint(p!!)
                m[1] = f
                return f
            } else
            if(position == 2) {
                val f =
                 PointDialogFragmentTargetXY.putExtra(
                    position
                ).setModel(pointDialogModelView)

                f.setPoint(p!!)
                m[2] = f
                return f
            } else
                if(position == 3) {
                    val f =
                        PointDialogFragmentTargetTwoDistances.putExtra(
                            position
                        ).setModel(pointDialogModelView)
                    f.setPoint(p!!)
                    m[3] = f
                    return f
                } else
            return PointDialogFragment.putExtra(
                position
            ).setModel(pointDialogModelView).setPoint(p!!)
            //(colorViewModel)

        }


        override fun getCount() = Point.PointType.values().size
    }

}