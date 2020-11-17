package com.example.travelrequestform.data.models

import android.annotation.SuppressLint
import android.location.Location
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


@Entity
class Travel {
    @PrimaryKey
    private var travelId : String? = null
    fun setTravelId(travelId : String){
        this.travelId = travelId
    }

    private val clientName: String? = null
    private val clientPhone: String? = null
    private val clientEmail: String? = null

    @TypeConverters(UserLocationConverter::class)
    private val travelLocations: List<UserLocation>? = null

    @TypeConverters(RequestType::class)
    private val requestType: RequestType? = null

    @TypeConverters(DateConverter::class)
    private val travelDate: Date? = null

    @TypeConverters(DateConverter::class)
    private val arrivalDate: Date? = null

    @TypeConverters(CompanyConverter::class)
    private val company: HashMap<String, Boolean>? = null

    class DateConverter {

        @SuppressLint("SimpleDateFormat")
        private var format = SimpleDateFormat("yyyy-MM-dd")
        @TypeConverter
        @Throws(ParseException::class)
        fun fromTimestamp(date: String?): Date? {
            return if (date == null) null else format.parse(date)
        }

        @TypeConverter
        fun dateToTimestamp(date: Date?): String? {
            return if (date == null) null else format.format(date)
        }
    }

    enum class RequestType(val code: Int) {
        SENT(0), ACCEPTED(1), RUN(2), CLOSE(3);

        companion object {
            @TypeConverter
            fun getType(numeral: Int): RequestType? {
                for (ds in values()) if (ds.code == numeral) return ds
                return null
            }

            @TypeConverter
            fun getTypeInt(requestType: RequestType?): Int? {
                return requestType?.code
            }
        }
    }

    class CompanyConverter {
        @TypeConverter
        fun fromString(value: String?): HashMap<String, Boolean>? {
            if (value == null || value.isEmpty()) return null
            val mapString =
                value.split(",").toTypedArray() //split map into array of (string,boolean) strings
            val hashMap = HashMap<String, Boolean>()
            for (s1 in mapString)  //for all (string,boolean) in the map string
            {
                if (!s1.isEmpty()) { //is empty maybe will needed because the last char in the string is ","
                    val s2 = s1.split(":")
                        .toTypedArray() //split (string,boolean) to company string and boolean string.
                    val aBoolean = java.lang.Boolean.parseBoolean(s2[1])
                    hashMap[s2[0]] = aBoolean
                }
            }
            return hashMap
        }

        @TypeConverter
        fun asString(map: HashMap<String?, Boolean?>?): String? {
            if (map == null) return null
            val mapString = StringBuilder()
            for ((key, value) in map) mapString.append(
                key
            ).append(":").append(value).append(",")
            return mapString.toString()
        }
    }

    class UserLocationConverter {
        @TypeConverter
        fun fromString(value: String?): UserLocation? {
            if (value == null || value == "") return null
            val lat = value.split(" ").toTypedArray()[0].toDouble()
            val long = value.split(" ").toTypedArray()[1].toDouble()
            return UserLocation(lat, long)
        }

        @TypeConverter
        fun asString(warehouseUserLocation: UserLocation?): String {
            return if (warehouseUserLocation == null) "" else warehouseUserLocation.longitude
                .toString() + " " + warehouseUserLocation.latitude
        }
    }

    class UserLocation(var latitude : Double, var longitude : Double)

}