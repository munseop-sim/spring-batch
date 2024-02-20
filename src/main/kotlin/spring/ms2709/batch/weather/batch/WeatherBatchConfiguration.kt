package spring.ms2709.batch.weather.batch

import jakarta.persistence.EntityManagerFactory
import org.slf4j.LoggerFactory
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.ItemWriter
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.orm.jpa.JpaTransactionManager
import spring.ms2709.batch.global.infrastructure.config.datasource.WeatherDataSourceConfig
import spring.ms2709.batch.weather.batch.reader.WeatherAddressItemReader
import spring.ms2709.batch.weather.infrastructure.entity.WeatherAddress
import spring.ms2709.batch.weather.infrastructure.repository.WeatherAddressRepository
import spring.ms2709.batch.weather.infrastructure.repository.WeatherCollectTimeRepository

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
class WeatherBatchConfiguration (
    private val weatherCollectTimeRepository: WeatherCollectTimeRepository,
    private val weatherAddressRepository: WeatherAddressRepository
){
    private val log = LoggerFactory.getLogger(WeatherBatchConfiguration::class.java)
    companion object{
        const val WEATHER_COLLECT_JOB_NAME="weatherCollectTimeJob"
    }

// jpaCursorItemReader를 사용할 경우
//
//    fun step1Reader(
//        entityManagerFactory: EntityManagerFactory
//    ) = JpaCursorItemReader<WeatherAddress>().apply {
//             this.name = "weatherAddressItemReader"
//             this.setEntityManagerFactory(entityManagerFactory)
//             this.setQueryString("select t1 from WeatherAddress t1")
//         }



    fun step1Writer(): ItemWriter<WeatherAddress> {
        return ItemWriter {
            log.info("target(${it.size()}) -> {}",it)
            it
        }
    }


    @Bean(WEATHER_COLLECT_JOB_NAME)
    fun importUserJob(
        listener: WeatherCollectCompletedListener,
        jobRepository: JobRepository,
        @Qualifier(WeatherDataSourceConfig.WEATHER_JPA_TRANSACTION_MANAGER) jpaTransactionManager: JpaTransactionManager,
        @Qualifier(WeatherDataSourceConfig.WEATHER_ENTITY_MANAGER_FACTORY) jpaEntityManagerFactory: EntityManagerFactory,
    ): Job {
        val job = JobBuilder(WEATHER_COLLECT_JOB_NAME, jobRepository)
            .listener(listener)
            .start(step1(jobRepository,jpaTransactionManager,jpaEntityManagerFactory))
            .build()
        return job
    }

    fun step1(
        jobRepository: JobRepository,
        jpaTransactionManager: JpaTransactionManager,
        jpaEntityManagerFactory: EntityManagerFactory,
    ): Step {
//        Tasklet vs Chunk
//        Tasklet: 한 가지 이상의 CRUD가 발생(=비즈니스 로직)하는 task에 대해 일괄적으로 처리하는 경우, 채택한다. (복잡한 로직을 수행해야 하는 job일 경우, 채택)
//        Chunk: chunk 단위로 처리할 모든 record를 쭉 읽어들인 후, 모두 읽어들이는데 성공하면 한번에 Write하는 방식 (대용량 데이터에 대해 단순 처리할 경우, 채택)
        val step = StepBuilder("${WEATHER_COLLECT_JOB_NAME}_step1",jobRepository)
            .chunk<WeatherAddress, WeatherAddress>(2,jpaTransactionManager)
//            .reader(step1Reader(jpaEntityManagerFactory))
            .reader(WeatherAddressItemReader(weatherAddressRepository))
            .writer(step1Writer())
            .build()
        return step
    }

}




