package com.mapcalibration.mvvm.util

import android.content.Context
import android.content.SharedPreferences

object Configuration{

    private var PRIVATE_MODE = 0
    private val PREF_NAME = "mindorks-welcome"

    private val IS_MAP_CENTERED = Pair("is_map_centered", false)
    private val LAST_NAME = Pair("last_name", "pt1")
    private val KEEP_SCREEN_ON = Pair("keep_screen_on", false)
    private val LANGUAGE_ENGLISH = Pair("language_english", true)

    private lateinit var preferences: SharedPreferences

    fun init(context: Context){
        preferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
    }

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit){
        val editor = edit()
        operation(editor)
        editor.apply()
    }


    var isMapCentered : Boolean
        get() = preferences.getBoolean(IS_MAP_CENTERED.first, IS_MAP_CENTERED.second)

        set(value) = preferences.edit {
            it.putBoolean(IS_MAP_CENTERED.first, value)
        }

    var keepScreenOn : Boolean
        get() = preferences.getBoolean(KEEP_SCREEN_ON.first, KEEP_SCREEN_ON.second)

        set(value) = preferences.edit {
            it.putBoolean(KEEP_SCREEN_ON.first, value)
        }

    var languageEnglish : Boolean
        get() = preferences.getBoolean(LANGUAGE_ENGLISH.first, LANGUAGE_ENGLISH.second)

        set(value) = preferences.edit {
            it.putBoolean(LANGUAGE_ENGLISH.first, value)
        }

    var lastName: String
        get() = preferences.getString(LAST_NAME.first, LAST_NAME.second)

        set(value) = preferences.edit{
            it.putString(LAST_NAME.first, value)
        }
}
