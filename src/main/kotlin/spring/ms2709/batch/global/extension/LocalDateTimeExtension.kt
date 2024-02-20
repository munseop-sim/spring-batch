package spring.ms2709.batch.global.extension

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 *
 * 클래스 설명
 *
 * @class LocalDateTimeExtension
 * @author 심문섭
 * @version 1.0
 * @since 2024-02-20 오후 11:31
 * @modified
 */
fun LocalDateTime.yyyyMMddWithTime():String{
    return this.format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"))
}