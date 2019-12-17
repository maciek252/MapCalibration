package com.mapcalibration.mvvm.ui.mapActivity.pointDialog



import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.mapcalibration.mvvm.R
import com.mapcalibration.mvvm.data.db.entity.Point
import com.mapcalibration.mvvm.databinding.DialogFragment1Binding

open  class PointDialogFragment : DialogFragment()
    //, AdapterView.OnItemSelectedListener
    //AdapterView.OnItemClickListener
    {
//    override fun onNothingSelected(p0: AdapterView<*>?) {
//
//    }


    open lateinit var  punk : Point

    private var binding : DialogFragment1Binding? = null

    var colorNo: Int = -1




    open lateinit var pointDialogModelView: PointDialogModelView

    companion object {
        fun putExtra(colorNo: Int) = PointDialogFragment().apply {
            arguments = Bundle().apply {

            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val arg = arguments ?: return

    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val v = inflater.inflate(R.layout.dialog_fragment1, null)
        binding = DataBindingUtil.bind(v)!!

        binding?.pointDialogModelView = this.pointDialogModelView






        return v.rootView
    }

//    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
//
//    }

    open fun savePoint(): Boolean {
        Log.d("savePoint", "abstract")
        return true
    }

    open fun setPoint(p: Point) : PointDialogFragment {
        punk = p
        return this
    }

    open fun  setModel(model: PointDialogModelView): PointDialogFragment {
        Log.d("ColorFragment", "setModel called")
        this.pointDialogModelView = model
        return this
    }

}