package com.leopold.mvvm.ui.mackowaActivity.mackowaActivity_pointDialog



import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import com.leopold.mvvm.R
import com.leopold.mvvm.data.db.entity.Point
import com.leopold.mvvm.databinding.DialogFragmentTargetTwoLinesBinding
import com.leopold.mvvm.databinding.DialogFragmentTargetTwoLinesBindingImpl

import com.leopold.mvvm.databinding.DialogFragmentTargetXyBinding

open class PointDialogFragmentTargetTwoDistances() : PointDialogFragment(), AdapterView.OnItemSelectedListener,
    AdapterView.OnItemClickListener{



    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    var binding : DialogFragmentTargetTwoLinesBinding? = null

    companion object {
        fun putExtra(colorNo: Int) = PointDialogFragmentTargetTwoDistances().apply {
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

        val v = inflater.inflate(R.layout.dialog_fragment_target_two_lines, null)
        binding = DataBindingUtil.bind(v)!!
        //binding.pointDialogModelView
        //DataBindingUtil.setContentView<>()
        //binding?.lifecycleOwner = this
        //binding.setLifecycleOwner { this }
        binding?.pointDialogModelView = this.pointDialogModelView

//        binding?.spinner!!.onItemSelectedListener = this
//        binding?.spinner2!!.onItemSelectedListener = this




        binding?.textViewDistanceToMarker1?.text = "" + punk.len1
        binding?.textViewDistanceToMarker2?.text = "" + punk.len2
        binding?.checkBox2?.isChecked = punk.rightFromLine

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
        binding?.spinner2?.setAdapter(aa)

        val name1 = pointDialogModelView.mackowaViewModel.points.value.find{it.id == punk.referenceId}?.name
        val i1 = lista.indexOfFirst{it == name1}
        i1.let{binding?.spinner?.setSelection(i1)}

        val name2 = pointDialogModelView.mackowaViewModel.points.value.find{it.id == punk.referenceId2}?.name
        val i2 = lista.indexOfFirst{it == name2}
        i2.let{binding?.spinner2?.setSelection(i2)}


        return v.rootView
    }

    override fun savePoint(){
        Log.d("savePoint", "txy")
        punk.len1 = binding?.textViewDistanceToMarker1?.text.toString().toDouble()
        punk.len2 = binding?.textViewDistanceToMarker2?.text.toString().toDouble()
        punk.rightFromLine = binding?.checkBox2?.isChecked!!


        val selectedReferenceName = binding?.spinner?.selectedItem.toString()
        val refId = pointDialogModelView.mackowaViewModel.points.value.filter{ it.name== selectedReferenceName}.first().id
        punk.referenceId = refId

        val selectedReferenceName2 = binding?.spinner2?.selectedItem.toString()
        val refId2 = pointDialogModelView.mackowaViewModel.points.value.filter{ it.name== selectedReferenceName2}.first().id
        punk.referenceId2 = refId2

        punk.pointType = Point.PointType.TARGET_TWO_DISTANCES
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


    override fun  setModel(model: PointDialogModelView): PointDialogFragmentTargetTwoDistances {
        Log.d("ColorFragment", "setModel called")
        this.pointDialogModelView = model

        return this
    }


}