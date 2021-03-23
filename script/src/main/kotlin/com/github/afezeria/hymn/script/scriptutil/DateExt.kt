package com.github.afezeria.hymn.script.scriptutil

import java.time.*
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.temporal.IsoFields
import java.time.temporal.TemporalAdjusters


/**
 * @author afezeria
 */
object DateExt {
    val yyyyMMddHHmmss = DateTimeFormatter
        .ofPattern("yyyy-MM-dd HH:mm:ss")

    val yyyyMMdd = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    val hhmmss = DateTimeFormatter.ofPattern("HH:mm:ss")

    val defaultZoneId = ZoneId.systemDefault()

    fun currentTimeMillis(): Long {
        return System.currentTimeMillis()
    }

    fun ofMillisSecond(millis: Long): LocalDateTime {
        val instant: Instant = Instant.ofEpochMilli(millis)
        return instant.atZone(defaultZoneId).toLocalDateTime()
    }

    fun now(): LocalDateTime {
        return LocalDateTime.now()
    }

    fun today(): LocalDate {
        return LocalDate.now()
    }

    fun yesterday(): LocalDate {
        return LocalDate.now().minusDays(1)
    }

    fun tomorrow(): LocalDate {
        return LocalDate.now().plusDays(1)
    }


    @JvmOverloads
    fun dayOfWeek(day: String? = null): Int {
        return if (day == null) {
            LocalDateTime.now().dayOfWeek.value
        } else if (day.length > 10) {
            LocalDateTime.parse(day, yyyyMMddHHmmss).dayOfWeek.value
        } else {
            LocalDate.parse(day, yyyyMMdd).dayOfWeek.value
        }
    }

    /**
     * 下一个星期[dow]的日期
     */
    fun nextWeekDay(dow: Int, day: LocalDate): LocalDate {
        val ta = TemporalAdjusters.next(DayOfWeek.of(dow))
        return day.with(ta)
    }

    fun nextWeekDay(dow: Int, day: LocalDateTime): LocalDate {
        val ta = TemporalAdjusters.next(DayOfWeek.of(dow))
        return day.with(ta).toLocalDate()
    }

    fun nextWeekDay(dow: Int, day: String): LocalDate {
        return if (day.length > 10) {
            nextWeekDay(dow, LocalDateTime.parse(day, yyyyMMddHHmmss))
        } else {
            nextWeekDay(dow, LocalDate.parse(day, yyyyMMdd))
        }
    }

    fun nextWeekDay(dow: Int): LocalDate {
        return nextWeekDay(dow, LocalDate.now())
    }

    fun weekOfYear(day: LocalDateTime): Int {
        return day.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR)
    }

    fun weekOfYear(day: LocalDate): Int {
        return day.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR)
    }

    fun weekOfYear(day: String): Int {
        return if (day.length > 10) {
            weekOfYear(LocalDateTime.parse(day, yyyyMMddHHmmss))
        } else {
            weekOfYear(LocalDate.parse(day, yyyyMMdd))
        }
    }

    fun weekOfYear(): Int {
        return weekOfYear(LocalDate.now())
    }

    fun parseDateTime(str: String): LocalDateTime? {
        return try {
            LocalDateTime.parse(str, yyyyMMddHHmmss)
        } catch (e: DateTimeParseException) {
            null
        }
    }

    fun parseDate(str: String): LocalDate? {
        return try {
            LocalDate.parse(str)
        } catch (e: DateTimeParseException) {
            null
        }
    }

    fun pareTime(str: String): LocalTime? {
        return try {
            LocalTime.parse(str)
        } catch (e: DateTimeParseException) {
            null
        }
    }

    fun format(time: LocalTime, format: String): String {
        return time.format(DateTimeFormatter.ofPattern(format))
    }

    fun format(date: LocalDate, format: String): String {
        return date.format(DateTimeFormatter.ofPattern(format))
    }

    fun format(dateTime: LocalDateTime, format: String): String {
        return dateTime.format(DateTimeFormatter.ofPattern(format))
    }
}