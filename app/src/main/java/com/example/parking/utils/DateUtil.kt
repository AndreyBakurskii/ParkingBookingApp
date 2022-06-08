package com.example.parking.utils

import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

fun Date.toStr(pattern: String): String {
    val simpleDateFormat: SimpleDateFormat = SimpleDateFormat(pattern, Locale.US)
    simpleDateFormat.timeZone = TimeZone.getTimeZone("GMT")

    return simpleDateFormat.format(this)
}

fun String.toDate(pattern: String): Date {
    val simpleDateFormat: SimpleDateFormat = SimpleDateFormat(pattern, Locale.US)
    simpleDateFormat.timeZone = TimeZone.getTimeZone("GMT")

    return simpleDateFormat.parse(this)!!
}

fun LocalDateTime.toDate() =  Date.from(this.atZone(ZoneId.systemDefault()).toInstant());

// var date = LocalDateTime.of(2022, 4, 20, 12, 32).toDate()