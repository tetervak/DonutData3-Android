package ca.tetervak.donutdata3.util

import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

private val dateFormatter =
    DateTimeFormatter.ofPattern("EEE, MMM dd, yyyy")

private val timeFormatter = DateTimeFormatter.ofPattern("h:mm a")

private val dateTimeFormatter =
    DateTimeFormatter.ofPattern("EEE, MMM dd, yyyy - h:mm a")

fun formatDate(date: Date?): String? {
    return date?.toInstant()
        ?.atZone(ZoneId.systemDefault())
        ?.toLocalDate()
        ?.format(dateFormatter)
}

fun formatTime(date: Date?): String? {
    return date?.toInstant()
        ?.atZone(ZoneId.systemDefault())
        ?.toLocalTime()
        ?.format(timeFormatter)
}

fun formatDateTime(date: Date?): String? {
    return date?.toInstant()
        ?.atZone(ZoneId.systemDefault())
        ?.toLocalDateTime()
        ?.format(dateTimeFormatter)
}