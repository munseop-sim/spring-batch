package spring.ms2709.batch.weather.infrastructure.repository.simple

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Repository
import spring.ms2709.batch.global.infrastructure.config.datasource.WeatherDataSourceConfig
import spring.ms2709.batch.weather.infrastructure.entity.QWeatherAddress
import spring.ms2709.batch.weather.infrastructure.entity.WeatherAddress

/**
 *
 * 클래스 설명
 *
 * @class WeatherAddressSimpleQuery
 * @author 심문섭
 * @version 1.0
 * @since 2024-02-20 오후 4:27
 * @modified
 */
interface WeatherAddressSimpleQuery {
    fun getAll(pageSize:Int,pageIndex:Int):List<WeatherAddress>
}


@Repository
class WeatherAddressSimpleQueryImpl(
    @Qualifier(WeatherDataSourceConfig.WEATHER_JPA_QUERY_FACTORY) private val  jpaQueryFactory: JPAQueryFactory,
):WeatherAddressSimpleQuery{
    private val tblWeatherAddress = QWeatherAddress.weatherAddress!!



    override fun getAll(pageSize: Int, pageIndex: Int): List<WeatherAddress> {
        return jpaQueryFactory.selectFrom(tblWeatherAddress)
            .offset(pageIndex * pageSize.toLong())
            .limit(pageSize.toLong())
            .fetch()
    }
}
