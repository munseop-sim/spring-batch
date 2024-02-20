package spring.ms2709.batch.weather.infrastructure.repository

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.Commit
import org.springframework.transaction.annotation.Transactional
import spring.ms2709.batch.global.infrastructure.config.datasource.WeatherDataSourceConfig

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

    @Commit
    @Test
    fun insertTest(){

    }
}