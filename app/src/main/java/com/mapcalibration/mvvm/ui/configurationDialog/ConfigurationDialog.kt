package com.mapcalibration.mvvm.ui.configurationDialog

import android.R
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter

import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment

import com.mapcalibration.mvvm.databinding.ConfigurationDialogBinding
import com.mapcalibration.mvvm.ui.mapActivity.MapActivity

import com.mapcalibration.mvvm.ui.mapActivity.MapViewModel
import com.mapcalibration.mvvm.util.Configuration
import com.mapcalibration.mvvm.util.LocaleManager.Companion.LANGUAGE_ENGLISH
import com.mapcalibration.mvvm.util.LocaleManager.Companion.LANGUAGE_POLISH


class ConfigurationDialog(val vm: MapViewModel) : DialogFragment(),  AdapterView.OnItemSelectedListener,
    AdapterView.OnItemClickListener, NoticeDialogFragment.NoticeDialogListener {

    val cd = this

    val TAG = "konfa"
    var userInteraction = false

     private lateinit var aa: ArrayAdapter<String>



    override fun onDialogPositiveClick(dialog: DialogFragment) {

        //Toast.makeText(dialog.context, "positive", Toast.LENGTH_LONG).show()


        val colName = binding?.spinnerCollections?.selectedItem.toString()
        Configuration.lastName = colName
        binding?.textViewCollectionName?.text = colName
        vm.readAllPoints(colName)
        //readAllCollectionNames()

        Log.d(TAG, "positive")
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {

        //Toast.makeText(dialog.context, "negative", Toast.LENGTH_LONG).show()
        Log.d(TAG, "negative")
    }

    private fun readAllCollectionNames(){
        val c = vm.getAllSavedCollections()
        //binding?.textViewCollections?.setText("" + c.toString())

//        // Set Adapter to Spinner
        aa.clear()
        aa.addAll(c)
        aa.notifyDataSetChanged()
        Log.d(TAG,"kolekcja=$c.toString()")

        binding?.textViewCollectionName?.text = Configuration.lastName

    }


    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
    }

    var binding : ConfigurationDialogBinding? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(com.mapcalibration.mvvm.R.layout.configuration_dialog, container, false)

        binding =  DataBindingUtil.bind(v)!!


        binding?.checkBoxCenterMapOnCurrentPosition?.setOnClickListener {
            Configuration.isMapCentered = binding?.checkBoxCenterMapOnCurrentPosition?.isChecked!!

            vm.centerMapOnGps.value = binding?.checkBoxCenterMapOnCurrentPosition?.isChecked

        }

        binding?.checkBoxSwitchOffScreen?.setOnClickListener {

            if(binding?.checkBoxSwitchOffScreen?.isChecked!!){
                Configuration.keepScreenOn = true
                vm.keepScreenOn.value = true
            } else {
                Configuration.keepScreenOn = false
                vm.keepScreenOn.value = false
            }
        }

        binding?.checkBoxSwitchOffScreen?.isChecked = vm.keepScreenOn.value!!

        binding?.buttonSave?.setOnClickListener {
            val collectionName = binding?.textViewCollectionName?.text.toString()
            Log.d(TAG, "saving...")
            Configuration.lastName = collectionName
            vm.saveAllPoints(collectionName)
            readAllCollectionNames()
        }




        binding?.spinnerCollections?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    Log.d(TAG, "klik=nothingSelected")
                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                    val element = parent?.getItemAtPosition(position)
                    Log.d(TAG, "klik$element")

                    if(userInteraction) {
                        val newFragment = NoticeDialogFragment()
                        newFragment.show(cd.childFragmentManager, "koty")
                    } else
                        userInteraction = true
                }

            }




        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        super.onViewCreated(view, savedInstanceState)

        //aa = ArrayAdapter(activity, R.layout.simple_spinner_item, emptyList<String>())
        aa = ArrayAdapter(activity as Context, R.layout.simple_spinner_item, mutableListOf())
        aa.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        //binding?.spinnerCollections?.setAdapter(aa)
        binding?.spinnerCollections?.adapter = aa

        readAllCollectionNames()

        binding?.lifecycleOwner = this

        if(vm.centerMapOnGps.value != null)
            binding?.checkBoxCenterMapOnCurrentPosition?.isChecked =
                vm.centerMapOnGps.value!!

        binding?.radioLanguageGroup?.clearCheck()

        if(Configuration.languageEnglish) {
//            binding?.radioButtonPolish?.isChecked = false
//            binding?.radioButtonEnglish?.isChecked = true
            //binding?.radioLanguageGroup?.check(binding?.radioLanguageGroup?.getChildAt(1)?.id!!)
            //binding?.radioLanguageGroup?.check(binding?.radioButtonEnglish?.id!!)
            binding?.radioButtonEnglish?.post {
                binding?.radioButtonEnglish?.isChecked = true
            }
//            (binding?.radioLanguageGroup?.getChildAt(1) as RadioButton).isChecked = true

        }else {
            //binding?.radioLanguageGroup?.check(binding?.radioLanguageGroup?.getChildAt(0)?.id!!)
            //binding?.radioLanguageGroup?.check(binding?.radioButtonPolish?.id!!)
//            binding?.radioButtonEnglish?.isChecked = false
//            binding?.radioButtonPolish?.isChecked = true

  //          (binding?.radioLanguageGroup?.getChildAt(0) as RadioButton).isChecked = true
            //binding?.radioLanguageGroup?.check(0)
            binding?.radioButtonPolish?.post {
                binding?.radioButtonPolish?.isChecked = true
            }
        }


        binding?.radioButtonPolish?.setOnClickListener {
            Configuration.languageEnglish = false
            val myActivity = context as MapActivity?
            myActivity?.setNewLocale(LANGUAGE_POLISH, false)

        }
        binding?.radioButtonEnglish?.setOnClickListener {
            Configuration.languageEnglish = true
            val myActivity = context as MapActivity?
            myActivity?.setNewLocale(LANGUAGE_ENGLISH, false)

        }

        binding?.buttonRemove?.setOnClickListener {
            Log.d(TAG, "removing...")
            vm.removeAllPointsFromCollectionAndCollectionIfIsNotTheLastOne()
            readAllCollectionNames()
        }

//        binding?.spinnerCollections?.setSelection(0,false)
//        binding?.spinnerCollections?.onItemSelectedListener =  object : AdapterView.OnItemSelectedListener{
//            override fun onNothingSelected(parent: AdapterView<*>?) {
//            }
//            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//            }
//        }

        val pos = aa.getPosition(Configuration.lastName)
        binding?.spinnerCollections?.setSelection(pos)

    }

}