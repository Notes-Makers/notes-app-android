package com.notesmakers.noteapp.extension

import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.TimeZone


const val PATTERN = "dd MMMM yyyy "


fun Long.localDateFromTimeStamp(): LocalDateTime = LocalDateTime.ofInstant(
    Instant.ofEpochMilli(this), TimeZone
        .getDefault().toZoneId()
)

fun formatLocalDateTimeToIsoString(localDateTime: LocalDateTime): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
    return localDateTime.format(formatter)
}