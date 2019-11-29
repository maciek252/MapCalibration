package com.mapcalibration.mvvm.util


import android.app.Activity
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Build

import java.lang.ref.WeakReference

import android.content.pm.PackageManager.GET_META_DATA
import android.os.Build.VERSION_CODES.P
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

object Utility {

    fun getStringFromResources(a: AppCompatActivity, name: String): String {
        fun AppCompatActivity.getStringFromRes(name: String):String {
            return resources.getString(resources.getIdentifier(name, "string", packageName))
        }
        return a.getStringFromRes(name)
    }

    // https://developer.android.com/about/versions/pie/restrictions-non-sdk-interfaces
    val titleCache: String
        get() {
            if (isAtLeastVersion(P)) return "Can't access title cache\nstarting from API 28"
            val o = Utility.getPrivateField(
                "android.app.ApplicationPackageManager",
                "sStringCache",
                null
            )
            val cache = o as Map<*, WeakReference<CharSequence>>? ?: return ""

            val builder = StringBuilder("Cache:").append("\n")
            for ((_, value) in cache) {
                val title = value.get()
                if (title != null) {
                    builder.append(title).append("\n")
                }
            }
            return builder.toString()
        }


    fun hexString(res: Resources): String {
        val resImpl = getPrivateField("android.content.res.Resources", "mResourcesImpl", res)
        val o = resImpl ?: res
        return "@" + Integer.toHexString(o.hashCode())
    }

    fun getPrivateField(className: String, fieldName: String, `object`: Any?): Any? {
        try {
            val c = Class.forName(className)
            val f = c.getDeclaredField(fieldName)
            f.isAccessible = true
            return f.get(`object`)
        } catch (e: Throwable) {
            e.printStackTrace()
            return null
        }

    }

    fun resetActivityTitle(a: Activity) {
        try {
            val info = a.packageManager.getActivityInfo(a.componentName, GET_META_DATA)
            if (info.labelRes != 0) {
                a.setTitle(info.labelRes)
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

    }

    fun getTopLevelResources(a: Activity): Resources {
        try {
            return a.packageManager.getResourcesForApplication(a.applicationInfo)
        } catch (e: PackageManager.NameNotFoundException) {
            throw RuntimeException(e)
        }

    }

    fun isAtLeastVersion(version: Int): Boolean {
        return Build.VERSION.SDK_INT >= version
    }

    fun setScreenStayOn(stayOn: Boolean, a: Activity) {
        if (stayOn) {

            a.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }else {
            a.window.clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        }
    }

}

