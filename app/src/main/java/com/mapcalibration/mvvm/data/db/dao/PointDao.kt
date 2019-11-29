package com.mapcalibration.mvvm.data.db.dao

import androidx.room.*
import com.mapcalibration.mvvm.data.db.entity.Point

/**
 * @author Maciej Szreter
 */

@Dao
interface PointDao {

    @Query("SELECT * FROM Point")
    fun findAllPoints(): Array<Point>
    //fun findAllPoints(): DataSource.Factory<Int, Point>

    @Query("DELETE FROM Point WHERE Point.Collection == :c")
    fun removeAllPointsFromCollection(c: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(point: Point)

    @Delete
    fun delete(point: Point)

}

