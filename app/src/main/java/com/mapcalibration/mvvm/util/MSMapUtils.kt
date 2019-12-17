package com.mapcalibration.mvvm.util

import android.location.Location
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import com.mapcalibration.mvvm.data.db.entity.Point
import kotlin.math.pow
import kotlin.math.sqrt

val NOT_VALID_LATLON : Double = 400.0

fun computeDistance(l1 : Location, l2: Location?, offsetX: Double, offsetY: Double) : Double {


    var result = ""
    result += l1.toString() + "<->" + l2?.toString()


    val pointDistanceTerrainMeters  = l1.distanceTo(l2)

    result += ":" + pointDistanceTerrainMeters.toString()
    result += "offsetX=" + offsetX + " offsetY=" + offsetY + "\n"
    val kwadrat  = offsetY.pow(2).plus( offsetX.pow(2))
    val pointDistanceCm= sqrt(kwadrat )
    result += "dist[cm]=" + pointDistanceCm


    result += "dist cm/m" + pointDistanceCm/pointDistanceTerrainMeters
    val metersPerCm : Double = 1.0/(pointDistanceCm/pointDistanceTerrainMeters)
    result += "dist m/cm" + metersPerCm

    println(result)
    return metersPerCm


}

fun computeTargetXY(p: Point, refOsnowa: Point, metersPerCm: Double){



    val l = Location("p1")
    l.latitude = p.latitude
    l.longitude = p.longitude
    val lBase = Location("p2")
    lBase.latitude = refOsnowa.latitude
    lBase.longitude = refOsnowa.longitude
        Log.d("Utils", "computing lampion position for pkt:" + p.id + "offsetX=" + p.x)
        val a = Location("dd")
        //a.latitude = 52.2;
        //a.longitude = 21.2;
        a.latitude = lBase.latitude
        a.longitude = lBase.longitude

        var headingX = 90.0
        var offX = p.x
        if(p.x < 0) {
            offX = -offX
            headingX = -90.0
        }

        var headingY = 0.0
        var offY = p.y
        if(offY < 0) {
            offY = -offY
            headingY = -180.0
        }

        val ln : LatLng = SphericalUtil.computeOffset(LatLng(a.latitude, a.longitude), offX * metersPerCm, headingX)
        val ln2 : LatLng = SphericalUtil.computeOffset(LatLng(ln.latitude, ln.longitude), offY * metersPerCm, headingY)
        //l.location = ln
        println("setting transformed lat" + ln.latitude)
        a.latitude = ln2.latitude
        a.longitude = ln2.longitude
        //computeOffset()

        //l.location = a
        p.longitude = a.longitude
        p.latitude = a.latitude




}

fun pointToLocation(p: Point) : Location  {
    val l1 = Location("p1")
    l1.latitude = p.latitude
    l1.longitude = p.longitude
    return l1
}

fun computeDistanceAndHeadingToCurrentPoint(target: Location, current: Location): Pair<Double, Double>{

    val dist = current.distanceTo(target)
    val dir = current.bearingTo(target)

    return Pair(dist.toDouble(), dir.toDouble())

}

fun computeAngle(a : Double, b : Double, c: Double) : Double {
    val m = (c*c - a*a - b*b)/(-2.0*a*b)
    val mm = Math.acos(m) * 180/Math.PI
    return mm
}

/*
fun addNewOsnowaPointWithValidPosFromMarker(relativeToNr1: Int, len1: Double, relativeToNr2: Int, len2: Double, odl: Double, leftUpper: Boolean = false) : Unit{

    val l1: GeoLocation? = Repozytorium.locationPositions[relativeToNr1];
    val l2: GeoLocation? = Repozytorium.locationPositions[relativeToNr2];

    val dist = l1!!.location!!.distanceTo(l2!!.location);
    val dir = l1!!.location!!.bearingTo(l2!!.location);


    println("odl=" + odl);
    val odlNaMapie = odl * dist;
    println("dist=" + dist + " odlNaMapie=" + odlNaMapie);

    val angle = computeAngle(len1, odlNaMapie, len2)
    println("angle=" + angle);


    var angleResult = dir + angle;
    if(leftUpper)
        angleResult = dir - angle;
    val l: LatLng = SphericalUtil.computeOffset(LatLng(l1!!.location!!.latitude, l1!!.location!!.longitude), len1*1000.0/(odl*1000.0), angleResult);
    println("computed location = " + l.toString());
    //val l: Location = l1.location.

    var lok = Location("")

    lok.longitude = l.longitude;
    lok.latitude = l.latitude;

    val nowa = GeoLocation(lok, 0.0, 0.0, true, 0, relativeToNr1, PointType.LAMPION_OFF);
    addNewPointToRepo(nowa)

    //val nowa = GeoLocation(loc, offsetX, offsetY, true, 0, relativeToNr, PointType.OSNOWA_MARKER);
    //return addNewPointToRepo(nowa)
}
*/

fun computeTarget2Distances(p: Point, p1: Point,  p2: Point, metersPerCm: Double) {

    val leftUpper = p.rightFromLine
    val odl = 1.0/metersPerCm

    val l1 = pointToLocation(p1)
    val l2 = pointToLocation(p2)

    val dist = l1.distanceTo(l2)
    val dir = l1.bearingTo(l2)

    val odlNaMapie = odl * dist


    val angle = computeAngle(p.len1, odlNaMapie, p.len2)
    println("angle=" + angle)

    var angleResult = dir + angle
    if(leftUpper)
        angleResult = dir - angle

    val l: LatLng = SphericalUtil.computeOffset(LatLng(l1.latitude, l1.longitude), p.len1*1000.0/(odl*1000.0), angleResult)

    p.longitude = l.longitude
    p.latitude = l.latitude




}
