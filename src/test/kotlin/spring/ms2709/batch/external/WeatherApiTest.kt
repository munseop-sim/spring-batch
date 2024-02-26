package spring.ms2709.batch.external

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spring.ms2709.batch.external.dto.datagokr.TimeWeatherParam
import spring.ms2709.batch.global.infrastructure.delegate.LogDelegate
import java.time.LocalDateTime

/**
 *
 * 클래스 설명
 *
 * @class WeatherApiImplTest
 * @author 심문섭
 * @version 1.0
 * @since 2024-02-21 오전 9:25
 * @modified
 */
@SpringBootTest
class WeatherApiTest @Autowired constructor(
    private val sut:WeatherApi
){

    private val log by LogDelegate()

    @DisplayName("지역의 시간대별 날씨를 가져올 수 있다.")
    @Test
    fun getTimeWeatherTest(){
        //given
        val param = TimeWeatherParam(targetTime = LocalDateTime.now().minusHours(2L), locationName = "test", x = 58, y = 125)

        //when
        val result = sut.getTimeWeather(param)

        //then
        assertThat(result).isNotNull
        log.info("result-> {}", result)
    }

    @DisplayName("잘못된 파라미터 전송시에 날씨를 가져올 수 없다.")
    @Test
    fun getTimeWeatherWithInvalidParam(){
        //given
        val param = TimeWeatherParam(targetTime = LocalDateTime.now().minusHours(2L), locationName = "test", x = 1, y = 1)

        //when
        val result = sut.getTimeWeather(param)

        //then
        assertThat(result).isNull()
    }

}