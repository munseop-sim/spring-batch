package spring.ms2709.batch.weather.application.usecase

import spring.ms2709.batch.weather.infrastructure.entity.WeatherAddress

/**
 *
 * 클래스 설명
 *
 * @class WeatherUsecase
 * @author 심문섭
 * @version 1.0
 * @since 2024-02-20 오후 4:23
 * @modified
 */
interface WeatherUsecase {
    fun getAllWeatherAddress():List<WeatherAddress>
}