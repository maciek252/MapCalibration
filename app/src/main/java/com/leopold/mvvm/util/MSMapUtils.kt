package com.leopold.mvvm.util

import android.location.Location
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import com.leopold.mvvm.data.db.entity.Point
import kotlin.math.pow
import kotlin.math.sqrt

fun computeDistance(l1 : Location, l2: Location?, offsetX: Double, offsetY: Double) : Double {

    ;
    var result: String = "";
    result += l1.toString() + "<->" + l2?.toString();


    val pointDistanceTerrainMeters  = l1?.distanceTo(l2)

    result += ":" + pointDistanceTerrainMeters.toString()
    result += "offsetX=" + offsetX + " offsetY=" + offsetY + "\n"
    val kwadrat  = offsetY?.pow(2).plus( offsetX.pow(2));
    val pointDistanceCm= sqrt(kwadrat )
    result += "dist[cm]=" + pointDistanceCm;


    result += "dist cm/m" + pointDistanceCm/pointDistanceTerrainMeters!!
    val metersPerCm : Double = 1.0/(pointDistanceCm/pointDistanceTerrainMeters!!)
    result += "dist m/cm" + metersPerCm

    println(result)
    return metersPerCm


}

fun computeTarget(p: Point, refOsnowa: Point, metersPerCm: Double){



    val l: Location = Location("p1")
    l.latitude = p.latitude
    l.longitude = p.longitude
    val lBase: Location = Location("p2")
    lBase.latitude = refOsnowa.latitude
    lBase.longitude = refOsnowa.longitude
        Log.d("Utils", "computing lampion position for pkt:" + p.id + "offsetX=" + p.x)
        var a = Location("dd")
        //a.latitude = 52.2;
        //a.longitude = 21.2;
        a.latitude = lBase!!.latitude;
        a.longitude = lBase!!.longitude;

        var headingX = 90.0;
        var offX = p.x;
        if(p.x < 0) {
            offX = -offX;
            headingX = -90.0;
        }

        var headingY = 0.0;
        var offY = p.y;
        if(offY < 0) {
            offY = -offY;
            headingY = -180.0;
        }

        val ln : LatLng = SphericalUtil.computeOffset(LatLng(a.latitude, a.longitude), offX * metersPerCm, headingX);
        val ln2 : LatLng = SphericalUtil.computeOffset(LatLng(ln.latitude, ln.longitude), offY * metersPerCm, headingY);
        //l.location = ln
        println("setting transformed lat" + ln.latitude)
        a.latitude = ln2.latitude
        a.longitude = ln2.longitude
        //computeOffset()

        //l.location = a
        p.longitude = a.longitude
        p.latitude = a.latitude




}