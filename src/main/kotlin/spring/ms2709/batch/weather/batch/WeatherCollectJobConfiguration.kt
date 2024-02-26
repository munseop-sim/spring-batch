package spring.ms2709.batch.weather.batch

import org.springframework.batch.core.*
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.ItemReader
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.orm.jpa.JpaTransactionManager
import spring.ms2709.batch.external.WeatherApi
import spring.ms2709.batch.external.dto.datagokr.TimeWeatherParam
import spring.ms2709.batch.external.dto.datagokr.TimeWeatherResult
import spring.ms2709.batch.global.infrastructure.config.datasource.WeatherDataSourceConfig
import spring.ms2709.batch.global.infrastructure.delegate.LogDelegate
import spring.ms2709.batch.weather.infrastructure.entity.WeatherAddress
import spring.ms2709.batch.weather.infrastructure.entity.WeatherCollectTime
import spring.ms2709.batch.weather.infrastructure.repository.WeatherAddressRepository
import spring.ms2709.batch.weather.infrastructure.repository.WeatherCollectTimeRepository
import spring.ms2709.batch.weather.infrastructure.repository.simple.WeatherAddressSimpleQuery
import java.time.LocalDateTime

/**
 *
 * 클래스 설명
 *
 * @class WeatherBatchConfiguration
 * @author 심문섭
 * @version 1.0
 * @since 2024-02-20 오후 3:31
 * @modified
 */
@Configuration
class WeatherCollectJobConfiguration (
    private val weatherApi: WeatherApi,
    private val weatherCollectTimeRepository: WeatherCollectTimeRepository,
    private val weatherAddressRepository: WeatherAddressRepository,
    private val weatherAddressSimpleQuery: WeatherAddressSimpleQuery,
    private val jobRepository: JobRepository,
    @Qualifier(WeatherDataSourceConfig.WEATHER_JPA_TRANSACTION_MANAGER) private val jpaTransactionManager: JpaTransactionManager,
){
    private val log by LogDelegate()
    private val CHUNK_SIZE =3
    companion object{
        const val WEATHER_COLLECT_JOB_NAME="weatherCollectTimeJob"
    }

    @Bean(WEATHER_COLLECT_JOB_NAME)
    fun weatherCollectTimeJob(
        @Qualifier("${WEATHER_COLLECT_JOB_NAME}_step_1") step1:Step
    ): Job {
        val job = JobBuilder(WEATHER_COLLECT_JOB_NAME, jobRepository)
            .listener(
                object: JobExecutionListener{
                    override fun afterJob(jobExecution: JobExecution) {
                        log.info("$WEATHER_COLLECT_JOB_NAME COMPLETED -> {}", jobExecution)
                    }
                }
            )
            .start(step1)
            .build()
        return job
    }

    @Bean("${WEATHER_COLLECT_JOB_NAME}_step_reader_1")
    fun weatherAddressReader():ItemReader<WeatherAddress>{
        return WeatherAddressReader(weatherAddressSimpleQuery, CHUNK_SIZE)
    }



    @Bean("${WEATHER_COLLECT_JOB_NAME}_step_1")
    fun step1(
        @Qualifier("${WEATHER_COLLECT_JOB_NAME}_step_reader_1") weatherAddressReader:ItemReader<WeatherAddress>
    ): Step {
        val step = StepBuilder("${WEATHER_COLLECT_JOB_NAME}_step1",jobRepository)
            .chunk<WeatherAddress, TimeWeatherResult?>(CHUNK_SIZE, jpaTransactionManager)
            .reader(weatherAddressReader)
            .processor {
                TimeWeatherParam(
                    targetTime = LocalDateTime.now(),
                    locationName = it.waAddress ?: "알 수 없는 위치",
                    x = it.waX!!,
                    y = it.waY!!,
                ).run {
                    weatherApi.getTimeWeather(this)
                }
            }
            .writer{
                it.items.map {
                    WeatherCollectTime(
                        totalData = it.totalData,
                        wctAddress = it.location,
                        measureTime = it.measureTime,
                        temperature = it.temperature?.toDouble() ?: null,
                        rainType = it.rainType
                    )
                }.run {
                    weatherCollectTimeRepository.saveAll(this)
                }.run {
                    log.info("write saved-> {}", this)
                }
            }.listener{
                object : StepExecutionListener {
                    override fun afterStep(stepExecution: StepExecution): ExitStatus? {
                        return super.afterStep(stepExecution)
                    }
                }
            }.build()
        return step
    }
}




