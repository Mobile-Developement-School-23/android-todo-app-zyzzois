package com.example.data.util

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

fun LocalDate.toLong(): Long {
    val zoneId = ZoneId.systemDefault()
    return atStartOfDay(zoneId).toEpochSecond()
}

fun dateFromLong(time: Long): LocalDate =
    Instant.ofEpochMilli(time).atZone(ZoneId.systemDefault()).toLocalDate()

fun LocalDate.toStringDate(): String =
    DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).format(this)

fun LocalDate.toShortStringDate(): String =
    DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).format(this)
