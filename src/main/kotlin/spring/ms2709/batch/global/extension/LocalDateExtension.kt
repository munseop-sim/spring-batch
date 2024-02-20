package spring.ms2709.batch.global.extension

import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 *
 * LocalDate 확장함수
 *
 * @class LocalDateExtension
 * @author 심문섭, ms2709@a2dcorp.co.kr
 * @version 1.0
 * @since 2023-12-18 오후 4:45
 * @modified
 */
fun LocalDate.toISOStringWithDayName():String{
    return "${this.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))} ${this.getDayOfWeek().getDisplayName(java.time.format.TextStyle.FULL, java.util.Locale.KOREAN)}"
}

fun LocalDate.toISOString():String{
    return this.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
}

fun LocalDate.yyyyMMdd():String{
    return this.format(DateTimeFormatter.ofPattern("yyyyMMdd"))
}