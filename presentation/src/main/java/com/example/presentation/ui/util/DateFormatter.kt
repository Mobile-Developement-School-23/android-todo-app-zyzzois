package com.example.presentation.ui.util

import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun dateFromLong(time: Long): LocalDate {
    return Instant.ofEpochMilli(time )
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
}

fun LocalDate.toStringDate(): String =
    DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).format(this)

fun LocalDate.toLong(): Long {
    val zoneId = ZoneOffset.UTC
    return atStartOfDay(zoneId).toEpochSecond() * 1000
}

fun getMillisFromHourAndMinutes(hour: Int, minutes: Int) =
    hour * 60 * 60 * 1000 + minutes * 60 * 1000

fun Long.toTextFormat(): String {
    val dateFormat = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())
    return dateFormat.format(this)
}

fun Long.getMillisAtMidnight(): Long {
    val calendar: Calendar = Calendar.getInstance(TimeZone.getDefault())

    calendar.timeInMillis = this;
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    return calendar.timeInMillis
}

fun convertLongDeathlineToString(TimeinMilliSeccond :Long) =  SimpleDateFormat("dd MMMM yyyy").format(
    Date(TimeinMilliSeccond)
)

fun convertMinutesToLong( TimeinMilliSeccond:Long,hour:Int,min:Int):Long{
    val year = SimpleDateFormat("yyyy").format(Date(TimeinMilliSeccond))
    val day = SimpleDateFormat("dd").format(Date(TimeinMilliSeccond))
    val mount = SimpleDateFormat("MM").format(Date(TimeinMilliSeccond))

    val date = SimpleDateFormat("yyyy MM dd hh mm").parse("$year $mount $day $hour $min")
    return date.time
}

fun convertIntTimeToString(hour: Int,min: Int): String {
    var ans = "00:00"
    if(hour>10){
        if(min>10) ans = "$hour:$min"
        else ans = "$hour:0$min"
    }
    else{
        if(min>10)ans = "0$hour:$min"
        else "0$hour:0$min"
    }
    return ans
}