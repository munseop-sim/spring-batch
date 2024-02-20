package spring.ms2709.batch.weather.application.service


import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import spring.ms2709.batch.global.infrastructure.config.datasource.WeatherDataSourceConfig
import spring.ms2709.batch.weather.application.usecase.WeatherUsecase
import spring.ms2709.batch.weather.infrastructure.entity.WeatherAddress

/**
 *
 * 클래스 설명
 *
 * @class WeatherService
 * @author 심문섭
 * @version 1.0
 * @since 2024-02-20 오후 4:23
 * @modified
 */
@Transactional(transactionManager = WeatherDataSourceConfig.WEATHER_JPA_TRANSACTION_MANAGER)
@Service
class WeatherService : WeatherUsecase {
    override fun getAllWeatherAddress(): List<WeatherAddress> {
        TODO("Not yet implemented")
    }
}