package com.leopold.mvvm.ui.mackowaActivity



import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.leopold.mvvm.R
import com.leopold.mvvm.data.db.entity.Point
import com.leopold.mvvm.databinding.DialogFragment1Binding
import com.leopold.mvvm.databinding.PointEditDialogBinding
import kotlinx.android.synthetic.main.dialog_fragment1.*

open class PointDialogFragment() : DialogFragment(), AdapterView.OnItemSelectedListener,
    AdapterView.OnItemClickListener{
    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    open lateinit var  punk : Point

    private var binding : DialogFragment1Binding? = null

    var colorNo: Int = -1



    var list_of_items = arrayOf("Item 1", "Item 2", "Item 3")

    open lateinit var pointDialogModelView: PointDialogModelView

    companion object {
        fun putExtra(colorNo: Int) = PointDialogFragment().apply {
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




        //DialogFragment1Binding.inflate(inflater, R.layout.dialog_fragment1, container, false)
        //DialogFragment1Binding

        val v = inflater.inflate(R.layout.dialog_fragment1, null)
        binding = DataBindingUtil.bind(v)!!
        //binding.pointDialogModelView
        //DataBindingUtil.setContentView<>()
        //binding?.lifecycleOwner = this
        //binding.setLifecycleOwner { this }
        binding?.pointDialogModelView = this.pointDialogModelView

        binding?.spinner!!.onItemSelectedListener = this

        val aa = ArrayAdapter(
            activity,
            R.layout.support_simple_spinner_dropdown_item,
            list_of_items
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        colorNameView.text = activity.getString(R.string.color_primary)
//        backgroundLayout.background = activity.getDrawable(R.color.colorPrimary)
//
//        colorViewModel.colorIdData.observe(this, Observer { bindColorId(it) })
//        colorViewModel.colorNameId.observe(this, Observer { bindColorNameId(it) })
    }

    //
    // setups
    //

    open fun setPoint(p: Point) : PointDialogFragment{
        punk = p
        return this
    }

    open fun  setModel(model: PointDialogModelView): PointDialogFragment {
        Log.d("ColorFragment", "setModel called")
        this.pointDialogModelView = model
        return this
    }

    open fun setModel(): PointDialogFragment {
        Log.d("ColorFragment", "setModel called")

        return this
    }

//    fun bindColorId(colorId: RColor?) {
//        colorId ?: return
//
//        backgroundLayout.background = activity.getDrawable(colorId)
//    }
//
//    fun bindColorNameId(colorNameId: RString?) {
//        colorNameId ?: return
//
//        colorNameView.text = activity.getString(colorNameId)
//    }
}