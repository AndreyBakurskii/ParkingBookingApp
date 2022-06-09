package com.example.parking.utils

import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

fun Date.toStr(pattern: String, timeZone: TimeZone): String {
    val simpleDateFormat: SimpleDateFormat = SimpleDateFormat(pattern, Locale.US)
    simpleDateFormat.timeZone = timeZone

    return simpleDateFormat.format(this)
}

fun String.toDate(pattern: String, timeZone: TimeZone = TimeZone.getTimeZone("GMT")): Date {
    val simpleDateFormat: SimpleDateFormat = SimpleDateFormat(pattern, Locale.US)
    simpleDateFormat.timeZone = timeZone

    return simpleDateFormat.parse(this)!!
}

fun LocalDateTime.toDate() =  Date.from(this.atZone(ZoneId.of("GMT")).toInstant());

// var date = LocalDateTime.of(2022, 4, 20, 12, 32).toDate()