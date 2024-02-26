package spring.ms2709.batch.weather.infrastructure.entity

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

/**
 *
 * 날씨수집데이터 저장 엔티티
 *
 * @class WeatherCollectTime
 * @author 심문섭
 * @version 1.0
 * @since 2024-02-20 오후 3:23
 * @modified
 */
@Table(name = "WEATHER_COLLECT_TIME")
@Entity
@EntityListeners(AuditingEntityListener::class)
class WeatherCollectTime {

    @Id
    @Column(name = "wct_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var wctId:Long? = null

    @Column(name = "wct_address", columnDefinition = "varchar(255)")
    var wctAddress:String? = null

    @Column(name = "wct_measure_time")
    var measureTime:LocalDateTime? = null

    @Column(name = "wct_temperature")
    var temperature:Double? = null

    @Column(name = "wct_rain_type")
    var rainType:String? = null

    @Column(name = "wct_total_data_json", columnDefinition = "json")
    var totalData:String? = null

    @CreatedDate
    var createdTime:LocalDateTime? = null

    constructor(
        wctAddress: String?,
        measureTime: LocalDateTime?,
        temperature: Double?,
        rainType: String?,
        totalData: String?
    ) {
        this.wctAddress = wctAddress
        this.measureTime = measureTime
        this.temperature = temperature
        this.rainType = rainType
        this.totalData = totalData
    }

    override fun toString(): String {
        return "WeatherCollectTime(wctId=$wctId, wctAddress=$wctAddress, measureTime=$measureTime, temperature=$temperature, rainType=$rainType, totalData=$totalData, createdTime=$createdTime)"
    }


}