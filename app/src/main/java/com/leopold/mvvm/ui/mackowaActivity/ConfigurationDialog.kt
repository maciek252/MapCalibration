package com.leopold.mvvm.ui.mackowaActivity

import android.R
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager

import com.leopold.mvvm.data.db.entity.Point
import com.leopold.mvvm.databinding.ConfigurationDialogBinding
import com.leopold.mvvm.databinding.PointEditDialogBinding
import com.leopold.mvvm.ui.MackowaActivity.MackowaActivity
import com.leopold.mvvm.ui.mackowaActivity.mackowaActivity_pointDialog.*
import com.leopold.mvvm.util.Configuration
import com.leopold.mvvm.util.LocaleManager.Companion.LANGUAGE_ENGLISH
import com.leopold.mvvm.util.LocaleManager.Companion.LANGUAGE_POLISH
import kotlinx.android.synthetic.main.configuration_dialog.*


class ConfigurationDialog(val vm: MackowaViewModel) : DialogFragment(),  AdapterView.OnItemSelectedListener,
    AdapterView.OnItemClickListener {



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        val c = vm.getAllSavedCollections()
        binding?.textViewCollections?.setText("kol=" + c.toString())

        val aa = ArrayAdapter(activity, R.layout.simple_spinner_item, c)
//        // Set layout to use when the list of choices appear
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        // Set Adapter to Spinner
        binding?.spinnerCollections?.setAdapter(aa)

        binding?.lifecycleOwner = this

        if(vm?.centerMapOnGps?.value != null)
            binding?.checkBoxCenterMapOnCurrentPosition?.isChecked =
                vm?.centerMapOnGps?.value!!

        binding?.radioButton?.setOnClickListener {
            val myActivity = context as MackowaActivity?
            myActivity?.setNewLocale(LANGUAGE_POLISH, false)
        }
        binding?.radioButton2?.setOnClickListener {
            val myActivity = context as MackowaActivity?
            myActivity?.setNewLocale(LANGUAGE_ENGLISH, false)
        }

    }

    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
    }




    public var binding : ConfigurationDialogBinding? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(com.leopold.mvvm.R.layout.configuration_dialog, container, false)

        binding =  DataBindingUtil.bind(v)!!


        binding?.checkBoxCenterMapOnCurrentPosition?.setOnClickListener {
            Configuration.isMapCentered = binding?.checkBoxCenterMapOnCurrentPosition?.isChecked!!

            vm?.centerMapOnGps?.value = binding?.checkBoxCenterMapOnCurrentPosition?.isChecked

        }


        binding?.buttonSave?.setOnClickListener {
            val collectionName = binding?.textViewCollectionName?.text.toString()
            vm.saveAllPoints(collectionName)
        }
        binding?.buttonRead?.setOnClickListener {

            val colName = binding?.spinnerCollections?.selectedItem.toString()

            vm.readAllPoints(colName)
        }


        return v
    }


}