package spring.ms2709.batch.external.dto.datagokr

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 *
 * 클래스 설명
 *
 * 사용 예 :
 * <pre>
 *   클래스 사용 예제
 *
 * </pre>
 *
 * @class WeatherTime
 * @author 심문섭, ms2709@a2dcorp.co.kr
 * @version 1.0
 * @since 2024-01-23 오후 4:29
 * @modified
 */


/**
 * 해당 지역의 시간대별 날씨 조회하기 위한 파라미터
 */
class TimeWeatherParam(
    var targetTime: LocalDateTime,
    var locationName:String,
    var x:Int,
    var y:Int
){
    fun getBaseDate():String{
        return if(this.targetTime.minute < 20){
            targetTime.minusHours(1L).format(DateTimeFormatter.ofPattern("yyyyMMdd")).trim()
        }else{
            targetTime.format(DateTimeFormatter.ofPattern("yyyyMMdd")).trim()
        }
    }

    fun getBaseTime():String{
        return if(this.targetTime.minute < 20){
            "${targetTime.minusHours(1L).format(DateTimeFormatter.ofPattern("HH"))}00".trim()
        }else{
            "${targetTime.format(DateTimeFormatter.ofPattern("HH"))}00".trim()
        }
    }



}

/**
 * 해당 지역의 시간대별 날씨 조회 결과
 */
data class TimeWeatherResult(
    val location:String,
    val measureTime: LocalDateTime,
    val temperature:Float?, //기온
    val rainType:String?,
    val totalData:String
)


/**
 * API 호출 결과
 */
data class TimeWeatherBodyDetailItem(
    var baseDate:String? = null,
    var baseTime:String? = null,
    var category:String? = null,
    var obsrValue:String? = null
){
    val categoryVal: TimeWeatherBodyDetailItemCategoryTypes?
            = TimeWeatherBodyDetailItemCategoryTypes.entries.firstOrNull { it.name == this.category }

    fun getValue():String?{
        this.obsrValue ?: return null
        return if(categoryVal == TimeWeatherBodyDetailItemCategoryTypes.PTY){
            return PTYValueType.entries.firstOrNull { it.ptyCode == this.obsrValue?.toInt() }?.caption
        }else{
            "${this.obsrValue ?: ""}${this.categoryVal?.unitName ?: ""}".trim()
        }
    }
}

enum class TimeWeatherBodyDetailItemCategoryTypes(val caption:String, val unitName:String){
    T1H("기온","℃"),
    RN1("1시간 강수량","mm"),
    UUU("동서바람성분","m/s"),
    VVV("남북바람성분","m/s"),
    REH("습도","%"),
    PTY("강수형태",""),
    VEC("풍향","deg"),
    WSD("풍속","m/s")
}

enum class PTYValueType(
    val ptyCode:Int,
    val caption:String
){
    EMPTY(0, "없음"),
    RAIN(1, "비"),
    RAIN_OR_SNOW(2, "비/눈"),
    SNOW(3, "눈"),
    SUDDEN_RAIN(4, "소나기"),
    RAIN_DROP(5, "빗방울"),
    SPLASHING_RAIN_DROP(6, "빗방울 날림"),
    SPLASHING_SNOW(7, "눈 날림");

    companion object{
        fun getByCode(code:Int): PTYValueType?{
            return PTYValueType.entries.firstOrNull { it.ptyCode == code }
        }
    }
}
