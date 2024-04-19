package com.notesmakers.noteapp.extension

import java.time.Instant
import java.time.LocalDateTime
import java.util.TimeZone


const val PATTERN = "dd MMMM yyyy "


fun Long.localDateFromTimeStamp(): LocalDateTime = LocalDateTime.ofInstant(
    Instant.ofEpochMilli(this), TimeZone
        .getDefault().toZoneId()
)