package com.leopold.mvvm.ui.mackowaActivity.mackowaActivity_pointDialog



import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import com.google.android.gms.maps.model.LatLng
import com.leopold.mvvm.R
import com.leopold.mvvm.data.db.entity.Point
import com.leopold.mvvm.databinding.DialogFragmentOsnowaXyBinding

open class PointDialogFragmentOsnowaXY() : PointDialogFragment(), AdapterView.OnItemSelectedListener,
    AdapterView.OnItemClickListener{



    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    var binding : DialogFragmentOsnowaXyBinding? = null

    companion object {
        fun putExtra(colorNo: Int) = PointDialogFragmentOsnowaXY().apply {
            arguments = Bundle().apply {
                putInt("colorNo", colorNo)
            }
        }
    }

    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val arg = arguments ?: return
        colorNo = arg.getInt("colorNo")
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val v = inflater.inflate(R.layout.dialog_fragment_osnowa_xy, null)
        binding = DataBindingUtil.bind(v)!!
        //binding.pointDialogModelView
        //DataBindingUtil.setContentView<>()
        //binding?.lifecycleOwner = this
        //binding.setLifecycleOwner { this }
        binding?.pointDialogModelView = this.pointDialogModelView

        binding?.spinner!!.onItemSelectedListener = this


        val l = LatLng(punk.latitude, punk.longitude)
        //pointDialogModelView?.mackowaViewModel?.latLngMarker.value = l // po co to ustawiac?


        binding?.textViewX?.text = "" + punk.len1

        val lista = pointDialogModelView.mackowaViewModel.points.value.filter{it.pointType == Point.PointType.OSNOWA_COORDINATES || it.pointType == Point.PointType.OSNOWA_MARKER_XY}.map{ it -> it.name}.toList()

        val aa = ArrayAdapter(
            activity,
            R.layout.support_simple_spinner_dropdown_item,
            //list_of_items
            lista
        )

            //val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, list_of_items)
        // Set layout to use when the list of choices appear
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Set Adapter to Spinner
        binding?.spinner?.setAdapter(aa)




        return v.rootView
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

    }

    override fun savePoint(){
        Log.d("savePoint", "osx")
        punk.len1 = binding?.textViewX?.text.toString().toDouble()

        punk.latitude = pointDialogModelView?.mackowaViewModel?.latLngMarker.value?.latitude!!
        punk.longitude = pointDialogModelView?.mackowaViewModel?.latLngMarker.value?.longitude!!

        punk.pointType = Point.PointType.OSNOWA_MARKER_XY
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    //
    // setups
    //
    override open fun  setModel(model: PointDialogModelView): PointDialogFragmentOsnowaXY {
        Log.d("ColorFragment", "setModel called")
        this.pointDialogModelView = model

        return this
    }
}