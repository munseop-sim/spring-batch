package spring.ms2709.batch.weather.infrastructure.repository

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import spring.ms2709.batch.global.infrastructure.config.datasource.WeatherDataSourceConfig
import spring.ms2709.batch.global.infrastructure.delegate.LogDelegate
import spring.ms2709.batch.weather.infrastructure.entity.WeatherAddress

/**
 *
 * 클래스 설명
 *
 * @class WeatherAddressRepositoryTest
 * @author 심문섭
 * @version 1.0
 * @since 2024-02-20 오후 5:17
 * @modified
 */
@Transactional(transactionManager = WeatherDataSourceConfig.WEATHER_JPA_TRANSACTION_MANAGER)
@SpringBootTest
class WeatherAddressRepositoryTest @Autowired constructor(
    private val sut:WeatherAddressRepository
){

    private val log by LogDelegate()

    @DisplayName("WeatherAddress을 INSERT 할 수 있다.")
    @Test
    fun insertTest(){
        //given
        val entity = WeatherAddress("test", 1,2,3)

        //when
        val result = sut.save(entity)

        //then
        assertThat(result.waId).isNotNull()
        log.info("result -> {}", result)
    }


}