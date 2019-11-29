package com.mapcalibration.mvvm.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.TypeConverters
import com.mapcalibration.mvvm.data.db.converter.DateConverter
import java.util.concurrent.ThreadLocalRandom

/**
 * @author Maciej Szreter
 */
@Entity(tableName = "Point", primaryKeys = arrayOf("id", "Collection"))
@TypeConverters(DateConverter::class)
data class Point(
    //@PrimaryKey(autoGenerate = true) var id: Int = this.idGlobal++,
    @ColumnInfo(name = "id") var id: Int = this.idGlobal++,
    @ColumnInfo(name = "name") var name: String = "pkt${id}",
    @ColumnInfo(name = "Collection") var collection: String = "kolekcja" ,
    //@Key(autoGenerate = true) var collection: String = "kolekcja",
    @ColumnInfo(name = "description") var description: String? = "desc",
    @ColumnInfo(name = "pointType") var pointType: PointType = PointType.OSNOWA_COORDINATES,
    @ColumnInfo(name = "latitude") var latitude: Double = ThreadLocalRandom.current().nextDouble(48.0, 53.0),
    @ColumnInfo(name = "longitude") var longitude: Double = ThreadLocalRandom.current().nextDouble(19.0, 23.0),
    @ColumnInfo(name = "number") var number: Int = 0,
    @ColumnInfo(name = "referenceId") var referenceId: Int = -1,
    @ColumnInfo(name = "referenceId2") var referenceId2: Int = -1,
    @ColumnInfo(name = "x") var x: Double = 0.0,
    @ColumnInfo(name = "y") var y: Double = 0.0,
    @ColumnInfo(name = "len1") var len1: Double = 0.0,
    @ColumnInfo(name = "len2") var len2: Double = 0.0,
    @ColumnInfo(name = "len1ref") var len1Ref: Int = 0,
    @ColumnInfo(name = "len2ref") var len2Ref: Int = 0,
    @ColumnInfo(name = "rightFromLine") var rightFromLine: Boolean = false,
    @ColumnInfo(name = "isValid") var isValid: Boolean = true
    //@ColumnInfo(name = "created") val created:  = LocalDateTime.now()
) {
    enum class PointType {OSNOWA_COORDINATES, OSNOWA_MARKER_XY, TARGET_XY, TARGET_TWO_DISTANCES}

    //var random = ThreadLocalRandom.current().nextDouble(min, max)

//    companion object {
//        fun to(repository: Repository): Point {
//            return Point(
//                name = repository.name,
//                description = repository.description,
//                //language = repository.language,
//                //stargazersCount = repository.stargazersCount,
//                created = Date()
//            )
//        }
//    }
companion object Point{
    var idGlobal: Int = 0
}
}
