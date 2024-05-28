package com.notesmakers.noteapp.extension

import java.time.Instant
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.TimeZone


const val PATTERN = "dd MMMM yyyy"


fun Long.zoneDateFromTimeStamp(): ZonedDateTime = ZonedDateTime.ofInstant(
    Instant.ofEpochMilli(this), TimeZone.getDefault().toZoneId()
)

fun toTimestampDataType(date: String): Long =
    parseStringToZonedDateTime(date).toInstant().toEpochMilli()

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