package com.mapcalibration.mvvm.ui.mapActivity.pointDialog



import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import com.mapcalibration.mvvm.R
import com.mapcalibration.mvvm.data.db.entity.Point

import com.mapcalibration.mvvm.databinding.DialogFragmentTargetXyBinding

open class PointDialogFragmentTargetXY() : PointDialogFragment(), AdapterView.OnItemSelectedListener,
    AdapterView.OnItemClickListener{



    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    var binding : DialogFragmentTargetXyBinding? = null

    companion object {
        fun putExtra(colorNo: Int) = PointDialogFragmentTargetXY().apply {
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





        val v = inflater.inflate(R.layout.dialog_fragment_target_xy, null)
        binding = DataBindingUtil.bind(v)!!
        //binding.pointDialogModelView
        //DataBindingUtil.setContentView<>()
        //binding?.lifecycleOwner = this
        //binding.setLifecycleOwner { this }
        binding?.pointDialogModelView = this.pointDialogModelView

        binding?.spinner!!.onItemSelectedListener = this



        binding?.textViewX?.text = "" + punk.x
        binding?.textViewY?.text = "" + punk.y

        val lista = pointDialogModelView.mapViewModel.points.value.filter{it.pointType == Point.PointType.OSNOWA_COORDINATES || it.pointType == Point.PointType.OSNOWA_MARKER_XY}.map{ it -> it.name}.toList()

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

    override fun savePoint(): Boolean {
        Log.d("savePoint", "txy")

        if(pointDialogModelView.mapViewModel.points.value.isEmpty())
            return false

        if(binding?.textViewX?.text.isNullOrBlank() || binding?.textViewY?.text.isNullOrBlank())
            return false

        if(binding?.spinner?.selectedItem == null)
            return false

        punk.x = binding?.textViewX?.text.toString().toDouble()
        punk.y = binding?.textViewY?.text.toString().toDouble()

        val selectedReferenceName = binding?.spinner?.selectedItem.toString()
        val refId = pointDialogModelView.mapViewModel.points.value.filter{ it.name== selectedReferenceName}.first().id
        punk.referenceId = refId

        punk.pointType = Point.PointType.TARGET_XY

        return true
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

    }


    //
    // setups
    //
    override open fun  setModel(model: PointDialogModelView): PointDialogFragmentTargetXY {
        Log.d("ColorFragment", "setModel called")
        this.pointDialogModelView = model

        return this
    }



}