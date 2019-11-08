package com.leopold.mvvm.ui.configurationDialog

import android.R
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment

import com.leopold.mvvm.databinding.ConfigurationDialogBinding
import com.leopold.mvvm.ui.MackowaActivity.MackowaActivity
import com.leopold.mvvm.ui.mackowaActivity.MackowaViewModel
import com.leopold.mvvm.util.Configuration
import com.leopold.mvvm.util.LocaleManager.Companion.LANGUAGE_ENGLISH
import com.leopold.mvvm.util.LocaleManager.Companion.LANGUAGE_POLISH


class ConfigurationDialog(val vm: MackowaViewModel) : DialogFragment(),  AdapterView.OnItemSelectedListener,
    AdapterView.OnItemClickListener, NoticeDialogFragment.NoticeDialogListener {

    val TAG = "konfa"

     lateinit var aa: ArrayAdapter<String>


    override fun onDialogPositiveClick(dialog: DialogFragment) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        Toast.makeText(dialog.context, "positive", Toast.LENGTH_LONG).show()
        Log.d(TAG, "positive")
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        Toast.makeText(dialog.context, "negative", Toast.LENGTH_LONG).show()
        Log.d(TAG, "negative")
    }

    fun readAllCollectionNames(){
        val c = vm.getAllSavedCollections()
        binding?.textViewCollections?.setText("" + c.toString())

//        // Set layout to use when the list of choices appear

//        // Set Adapter to Spinner
        aa.clear()
        aa.addAll(c)
        aa.notifyDataSetChanged()
        Log.d(TAG,"kolekcja=" + c.toString())

        binding?.textViewCollectionName?.text = Configuration.lastName

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
            Log.d(TAG, "saving...")
            Configuration.lastName = collectionName
            vm.saveAllPoints(collectionName)
            readAllCollectionNames()
        }
        binding?.spinnerCollections?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    Log.d(TAG, "klikus=nothingSelected")
                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val a = binding?.spinnerCollections?.adapter
                    val element = parent?.getItemAtPosition(position)
                    Log.d(TAG, "klikus=" + element)


                    val colName = binding?.spinnerCollections?.selectedItem.toString()
                    Configuration.lastName = colName
                    binding?.textViewCollectionName?.text = colName
                    vm.readAllPoints(colName)
                    //readAllCollectionNames()
                }

            }



        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        //aa = ArrayAdapter(activity, R.layout.simple_spinner_item, emptyList<String>())
        aa = ArrayAdapter(activity, R.layout.simple_spinner_item, mutableListOf())
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding?.spinnerCollections?.setAdapter(aa)

        readAllCollectionNames()



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

        binding?.buttonRemove?.setOnClickListener {
            Log.d(TAG, "removing...")
            vm?.removeAllPointsFromCollectionAndCollectionIfIsNotTheLastOne()
            readAllCollectionNames()
        }

        binding?.buttonRead?.setOnClickListener {

            val newFragment = NoticeDialogFragment()
            //newFragment.show(activity?.supportFragmentManager, "missiles")

            newFragment.show(this.childFragmentManager, "missiles")
        }
        val pos = aa?.getPosition(Configuration.lastName)
        binding?.spinnerCollections?.setSelection(pos)

    }

}