package spring.ms2709.batch.weather.infrastructure.repository

import org.springframework.data.jpa.repository.JpaRepository
import spring.ms2709.batch.weather.infrastructure.entity.WeatherAddress

/**
 *
 * 클래스 설명
 *
 * @class WeatherAddressRepository
 * @author 심문섭
 * @version 1.0
 * @since 2024-02-20 오후 4:11
 * @modified
 */
interface WeatherAddressRepository :JpaRepository<WeatherAddress, Long> {}
