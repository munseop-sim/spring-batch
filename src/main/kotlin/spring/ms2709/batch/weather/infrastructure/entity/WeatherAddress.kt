package spring.ms2709.batch.weather.infrastructure.entity

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

/**
 *
 * 클래스 설명
 *
 * @class WeatherAddress
 * @author 심문섭
 * @version 1.0
 * @since 2024-02-20 오후 4:09
 * @modified
 */
@Table(name = "WEATHER_ADDRESS")
@Entity
@EntityListeners(AuditingEntityListener::class)
class WeatherAddress {
    @Id
    @Column(name = "wa_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var waId:Long? = null

    @Column(name = "wa_address", columnDefinition = "varchar(255)")
    var waAddress:String? = null

    @Column(name = "wa_x")
    var waX:Int? = null

    @Column(name = "wa_y")
    var waY:Int? = null

    @Column(name = "wc_code")
    var wcCode:Int? = null

    @CreatedDate
    @Column(name="created_time")
    var createdTime: LocalDateTime? = null

    constructor(waAddress: String?, waX: Int?, waY: Int?, wcCode: Int?) {
        this.waAddress = waAddress
        this.waX = waX
        this.waY = waY
        this.wcCode = wcCode
    }


    override fun toString(): String {
        return "WeatherAddress(waId=$waId, waAddress=$waAddress, waX=$waX, waY=$waY, wcCode=$wcCode)"
    }


}