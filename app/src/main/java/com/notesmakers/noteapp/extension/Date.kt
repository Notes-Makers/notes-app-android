package com.notesmakers.noteapp.extension

import java.time.Instant
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.TimeZone


const val PATTERN = "dd MMMM yyyy "


fun Long.zoneDateFromTimeStamp(): ZonedDateTime = ZonedDateTime.ofInstant(
    Instant.ofEpochMilli(this), TimeZone.getDefault().toZoneId()
)

fun toTimestampDataType(date: String): Long =
    parseStringToZonedDateTime(date).toInstant().toEpochMilli()

fun formatZonedDateTimeToIsoString(zonedDateTime: ZonedDateTime): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX")
    return zonedDateTime.format(formatter)
}


fun main() {
    fun formatZonedDateTimeToIsoString(zonedDateTime: ZonedDateTime): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX")
        return zonedDateTime.format(formatter)
    }

    fun parseStringToZonedDateTime(dateTimeString: String?): ZonedDateTime {
        return try {
            ZonedDateTime.parse(dateTimeString)
        } catch (e: Exception) {
            System.currentTimeMillis().zoneDateFromTimeStamp()
        }
    }

    val zonedDateTime = ZonedDateTime.parse("2024-05-04T15:47:10Z")
    val isoString = formatZonedDateTimeToIsoString(zonedDateTime)
    val isoZString = parseStringToZonedDateTime(isoString).toInstant().toEpochMilli()
    val timeStamp = isoZString.zoneDateFromTimeStamp()

    println(timeStamp)
}

fun parseStringToZonedDateTime(dateTimeString: String?): ZonedDateTime {
    return try {
        ZonedDateTime.parse(dateTimeString)
    } catch (e: Exception) {
        System.currentTimeMillis().zoneDateFromTimeStamp()
    }
}