package spring.ms2709.batch.global.extension


/**
 *
 * String 클래스 확장 함수
 *
 * @class NumberExtension
 * @author 심문섭
 * @version 1.0
 * @since 2023-12-18 오후 4:46
 * @modified
 */



import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

fun String.genDownFileName():String{
    val currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"))
    return URLEncoder.encode("${currentTime}_${this}", StandardCharsets.UTF_8)
}

private val dateTimeFormatStringList = listOf("yyyy-MM-dd","yyyy-MM-dd HH:mm:ss","yyyy-MM-dd HH:mm", "yyyy/MM/dd HH:mm:ss", "yyyyMMdd", "M/d/yy", "M/d/yy H:mm" )

/**
 * 문자열을 LocalDateTime형식으로 변환
 */
fun String.toLocalDateTime(): LocalDateTime?{
    var converted: LocalDateTime? = null
    dateTimeFormatStringList.forEach {
        if(converted != null){
            return@forEach
        }

        converted = runCatching {
            LocalDateTime.parse(this, DateTimeFormatter.ofPattern(it))

        }.getOrNull()
    }
    return converted
}

/**
 * 문자열을 LocalDate 형식으로 변환
 */
fun String.toLocalDate(): LocalDate?{

    var converted: LocalDate? = null
    dateTimeFormatStringList.forEach {
        if(converted != null){
            return@forEach
        }

        converted = runCatching {
            LocalDate.parse(this, DateTimeFormatter.ofPattern(it))
        }.getOrNull()
    }
    return converted
}

fun String.toLocalTime():LocalTime?{
    var converted: LocalTime? = null
    listOf("HH00").forEach{
        converted = runCatching {
            LocalTime.parse(this, DateTimeFormatter.ofPattern(it))
        }.getOrNull()
    }
    return converted

}


/**
 * 문자열을 Int형식으로 변환
 */
fun String.parseInt():Int?{

    return runCatching {
        this.replace(",","").toDouble().toInt()
    }.getOrNull()
}

