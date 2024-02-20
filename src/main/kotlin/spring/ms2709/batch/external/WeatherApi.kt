package spring.ms2709.batch.external


import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import spring.ms2709.batch.external.dto.datagokr.*
import spring.ms2709.batch.global.extension.toLocalDate
import spring.ms2709.batch.global.extension.toLocalTime
import spring.ms2709.batch.global.extension.yyyyMMdd
import java.time.LocalDateTime

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
 * @class WeatherApi
 * @author 심문섭, ms2709@a2dcorp.co.kr
 * @version 1.0
 * @since 2024-01-23 오후 3:04
 * @modified
 */

class DataApiResult<T>(
    @field:JsonProperty("response")
    var result:T
)

class WeatherResponseResult<T>(
    var header:Map<String,String> = mapOf(),
    var body:T?
)

data class WeatherBody<T>(
    var dataType:String? = null,
    var pageNo:Int? = null,
    var numOfRows:Int? = null,
    var totalCount:Int? = null,
    var items: T
)

data class WeatherBodyItem<T>(
    var item:MutableList<T> = mutableListOf()
)

@FeignClient(name = "k-weather", url="https://apis.data.go.kr")
interface WeatherClient{
    @GetMapping("/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst")
    fun getTimeWeather(
        @RequestParam serviceKey:String,
        @RequestParam pageNo:Int = 1,
        @RequestParam numOfRows:Int = 1000,
        @RequestParam dataType:String = "JSON",
        @RequestParam(name = "base_date") baseDate:String,
        @RequestParam(name= "base_time") baseTime:String,
        @RequestParam nx:Int,
        @RequestParam ny:Int
    ) : DataApiResult<WeatherResponseResult<WeatherBody<WeatherBodyItem<TimeWeatherBodyDetailItem>>>>

    @GetMapping("/1360000/AsosDalyInfoService/getWthrDataList")
    fun getDayWeather(
        @RequestParam serviceKey:String,
        @RequestParam pageNo:Int = 1,
        @RequestParam numOfRows:Int = 10,
        @RequestParam dataType:String = "JSON",
        @RequestParam dataCd:String = "ASOS",
        @RequestParam dateCd:String = "DAY",
        @RequestParam startDt:String,
        @RequestParam endDt:String,
        @RequestParam stnIds:Int
    ):DataApiResult<WeatherResponseResult<WeatherBody<WeatherBodyItem<WeatherDayResult>>>>
}


interface WeatherApi{
    fun getTimeWeather(param: TimeWeatherParam): TimeWeatherResult?
    fun getDayWeather(param: WeatherDayParam):String?
}
//
@Component
class WeatherApiImpl(
    private val weatherClient:WeatherClient
) :WeatherApi{

    @Value("\${external-api-key.data-go-kr}")
    private lateinit var openApiKey:String

    override fun getTimeWeather(param:TimeWeatherParam):TimeWeatherResult?{
        val apiResponse = weatherClient.getTimeWeather(
            serviceKey = this.openApiKey,
            baseDate = param.getBaseDate(),
            baseTime = param.getBaseTime(),
            nx = param.x,
            ny = param.y
        )

        val items = apiResponse.result.body?.items?.item ?: mutableListOf()
        val firstItem = items.first()
        val measureTime = firstItem.run {
             this.baseDate?.toLocalDate()
        }?.let {
            LocalDateTime.of(it, firstItem.baseTime?.toLocalTime())
        }

        return TimeWeatherResult(
            location = param.locationName,
            measureTime = measureTime ?: param.targetTime,
            temperature = items.firstOrNull { it.categoryVal == TimeWeatherBodyDetailItemCategoryTypes.T1H }?.let { it.obsrValue?.toFloat() },
            rainType = items.firstOrNull { it.categoryVal == TimeWeatherBodyDetailItemCategoryTypes.PTY }?.let {   PTYValueType.getByCode(it.obsrValue?.toInt() ?: -1)?.caption },
            totalData = items.mapNotNull {
                "${it.categoryVal?.caption ?: ""}: ${it.getValue()}".run {
                    if(this.isNullOrBlank()){
                        null
                    }else{
                        this
                    }
                }
            }.joinToString(",")
        )
    }

    override fun getDayWeather(param: WeatherDayParam): String? {
        val apiResponse = weatherClient.getDayWeather(
            serviceKey = this.openApiKey,
            startDt = param.targetDate.yyyyMMdd(), endDt = param.targetDate.yyyyMMdd(), stnIds = param.wcCode)

        return apiResponse.result.body?.items?.item?.firstOrNull()?.toString() ?: null
    }


}
