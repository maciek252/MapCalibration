package com.leopold.mvvm.ui.mackowaActivity.mackowaActivity_pointDialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager

import com.leopold.mvvm.data.db.entity.Point
import com.leopold.mvvm.databinding.PointEditDialogBinding
import com.leopold.mvvm.ui.mackowaActivity.MackowaViewModel


class PointDialog(val vm: MackowaViewModel, var p: Point?) : DialogFragment(),  AdapterView.OnItemSelectedListener,
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

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(com.leopold.mvvm.R.layout.point_edit_dialog, container, false)
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

        //binding?.textView?.setText()
        binding?.buttonSavePoint?.setOnClickListener {




            p?.name = binding?.textViewPointName?.text.toString()

            if(wasEmpty)
                addPoint()

    // to nie wystarcza - adapter nie wykonuje odswiezenia
            //binding?.vm?.points = binding?.vm?.points!!
            // DO POPRAWKI!!!! PASKUDNE!
            val m = vm.points.value
            vm.points.value += p!!
            vm.points.value = m

            //vm?.points?.value -= p!!




            this.dismiss()
        }

        pointDialogModelView._punkt.value = p

        initializePageViewer(v)
        initializeIndicator()


        if(wasEmpty){
            if(vm.points.value.isEmpty()){

            } else
            if(vm.points.value.filter{it.pointType == Point.PointType.OSNOWA_MARKER_XY}.isEmpty()) {
                val tab =
                    binding?.indicatorLayout2?.getTabAt(Point.PointType.OSNOWA_MARKER_XY?.ordinal!!)
                tab?.select()
            } else  {
                val tab =
                    binding?.indicatorLayout2?.getTabAt(Point.PointType.ZWYKLY_XY?.ordinal!!)
                tab?.select()
            }
        } else {
            val tab = binding?.indicatorLayout2?.getTabAt(p?.pointType?.ordinal!!)
            tab?.select()
        }




        val listaTypow = Point.PointType.values().toList()
        var list_of_items = arrayOf("Item 1", "Item 2", "Item 3")
//        val aa = ArrayAdapter(
//            activity,
//            R.layout.support_simple_spinner_dropdown_item,
//            listaTypow
//        )
//
//        //val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, list_of_items)
//        // Set layout to use when the list of choices appear
//        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        // Set Adapter to Spinner
//        binding?.spinnerPointType?.setAdapter(aa)




        return v
    }

    private fun initializePageViewer(v : View) {

        val s = childFragmentManager

        //val s = getActivity()?.getSupportFragmentManager()

        if(binding?.pagerView != null){
            Log.d("WW", "pagerView != null")
        }
        binding?.pagerView?.adapter = ColorPagerAdapter(s!!)
        binding?.pagerView?.offscreenPageLimit = 2
        binding?.pagerView?.addOnPageChangeListener(pageChangeListener)
    }

    private fun initializeIndicator() {

        val listaTypow = Point.PointType.values().toList()
        binding?.indicatorLayout2?.setupWithViewPager(binding?.pagerView, true)
        for (tabNo in 0..listaTypow.size-1) {
            //indicatorLayout.getTabAt(tabNo)?.icon = ContextCompat.getDrawable(this, R.drawable
            //    .serduszko)
            //indicatorLayout2.getTabAt(tabNo)?.icon = ContextCompat.getDrawable(this, R.drawable
              //  .selector_indicator2)
            binding?.indicatorLayout2?.getTabAt(tabNo)?.text = "zolw"+listaTypow.get(tabNo)
        }
    }

    private val pageChangeListener = object : ViewPager.SimpleOnPageChangeListener() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            //colorViewModel.setColorNo(position)
        }
    }

    private inner class ColorPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int) : PointDialogFragment {
            //if(position == 1)
                //return ColorFragment.putExtra(position).setModel(colorViewModel)
            //else if(position == 0)
//                return ColorFragmentMackowy.putExtraMackowy(position).setModel(colorViewModel)
//            return ColorFragmentMackowy.putExtraMackowy(position).setModel(colorViewModel)
            //return ColorFragment.putExtra(position).setModel(colorViewModel)
            //return ColorFragmentMackowy.putExtraMackowy(position).setModel(colorViewModel)
            if(position == 0)
                return PointDialogFragmentOsnowaCoordinates.putExtra(
                    position
                ).setModel(pointDialogModelView).setPoint(p!!)
            if(position == 1)
                return PointDialogFragmentOsnowaXY.putExtra(
                    position
                ).setModel(pointDialogModelView).setPoint(p!!)
            if(position == 2)
                return PointDialogFragmentTargetXY.putExtra(
                    position
                ).setModel(pointDialogModelView).setPoint(p!!)
            return PointDialogFragment.putExtra(
                position
            ).setModel(pointDialogModelView).setPoint(p!!)
            //(colorViewModel)

        }


        override fun getCount() = 3
    }

}