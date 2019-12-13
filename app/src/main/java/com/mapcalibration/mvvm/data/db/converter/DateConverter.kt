package com.mapcalibration.mvvm.data.db.converter

import androidx.room.TypeConverter
import com.mapcalibration.mvvm.data.db.entity.Point


/**
 * @author Maciej Szreter
 */
class DateConverter {
    //@TypeConverter
    //fun toDate(value: Long): Date = Date(value)

    //@TypeConverter
    //fun toLong(date: Date): Long = date.time

    @TypeConverter
    fun toPointType(l: Int): Point.PointType = Point.PointType.values()[l]

    @TypeConverter
    fun toInt(type: Point.PointType): Int = type.ordinal
}
