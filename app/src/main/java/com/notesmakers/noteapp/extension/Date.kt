package com.notesmakers.noteapp.extension

import java.time.Instant
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.TimeZone


const val PATTERN = "dd MMMM yyyy "


fun Long.zoneDateFromTimeStamp(): ZonedDateTime = ZonedDateTime.ofInstant(
    Instant.ofEpochMilli(this), TimeZone.getDefault().toZoneId()
)

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
    val zonedDateTime = ZonedDateTime.parse("2021-09-30T15:30:00+01:00")
    val isoString = formatZonedDateTimeToIsoString(zonedDateTime)
    println(isoString)
    val isoZString = parseStringToZonedDateTime(isoString)
    println(isoZString)
}

fun parseStringToZonedDateTime(dateTimeString: String?): ZonedDateTime {
    return try {
        ZonedDateTime.parse(dateTimeString)
    } catch (e: Exception) {
        System.currentTimeMillis().zoneDateFromTimeStamp()
    }
}