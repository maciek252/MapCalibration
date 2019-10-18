package com.leopold.mvvm.ui.mackowaActivity.mackowaActivity_pointDialog



import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import com.leopold.mvvm.R
import com.leopold.mvvm.data.db.entity.Point
import com.leopold.mvvm.databinding.DialogFragmentOsnowaCoordinatesBinding

open class PointDialogFragmentOsnowaCoordinates() : PointDialogFragment(), AdapterView.OnItemSelectedListener,
    AdapterView.OnItemClickListener{
    override fun onNothingSelected(p0: AdapterView<*>?) {
        //pointDialogModelView.mackowaViewModel.latLngMarker.value.latitude
    }

    public var binding : DialogFragmentOsnowaCoordinatesBinding? = null



    companion object {
        fun putExtra(colorNo: Int) = PointDialogFragmentOsnowaCoordinates().apply {
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

        val v = inflater.inflate(R.layout.dialog_fragment_osnowa_coordinates, null)
        binding = DataBindingUtil.bind(v)!!
        //binding.pointDialogModelView
        //DataBindingUtil.setContentView<>()
        //binding?.lifecycleOwner = this
        //binding.setLifecycleOwner { this }
        binding?.pointDialogModelView = this.pointDialogModelView



        return v.rootView
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

    }

    override fun savePoint(){
        Log.d("savePoint", "oc")
        punk.latitude = pointDialogModelView?.mackowaViewModel?.latLngMarker.value?.latitude!!
        punk.longitude = pointDialogModelView?.mackowaViewModel?.latLngMarker.value?.longitude!!

        punk.pointType = Point.PointType.OSNOWA_COORDINATES
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    //
    // setups
    //
    override open fun  setModel(model: PointDialogModelView): PointDialogFragmentOsnowaCoordinates {
        Log.d("ColorFragment", "setModel called")
        this.pointDialogModelView = model
        return this
    }

}