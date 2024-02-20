package spring.ms2709.batch.weather.infrastructure.repository

import org.springframework.data.jpa.repository.JpaRepository
import spring.ms2709.batch.weather.infrastructure.entity.WeatherCollectTime

/**
 *
 * WeatherCollectTime JPA Repository
 *
 * @class WeatherCollectTimeRepository
 * @author 심문섭
 * @version 1.0
 * @since 2024-02-20 오후 3:30
 * @modified
 */
interface WeatherCollectTimeRepository : JpaRepository<WeatherCollectTime, Long> {}