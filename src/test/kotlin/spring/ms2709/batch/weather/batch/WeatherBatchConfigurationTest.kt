package spring.ms2709.batch.weather.batch

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.batch.core.BatchStatus
import org.springframework.batch.core.Job
import org.springframework.batch.test.JobLauncherTestUtils
import org.springframework.batch.test.context.SpringBatchTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.transaction.annotation.Transactional
import spring.ms2709.batch.global.infrastructure.config.BatchConfiguration
import spring.ms2709.batch.global.infrastructure.config.datasource.MetaDataSourceConfig
import spring.ms2709.batch.global.infrastructure.config.datasource.WeatherDataSourceConfig
import spring.ms2709.batch.weather.infrastructure.entity.WeatherAddress
import spring.ms2709.batch.weather.infrastructure.repository.WeatherAddressRepository


/**
 *
 * 클래스 설명
 *
 * @class WeatherBatchConfigurationTest
 * @author 심문섭
 * @version 1.0
 * @since 2024-02-20 오후 3:52
 * @modified
 */

@ComponentScan(basePackages = ["spring.ms2709.batch.weather"])
@EnableAutoConfiguration
@SpringBatchTest
@SpringBootTest(classes = [BatchConfiguration::class, WeatherDataSourceConfig::class, MetaDataSourceConfig::class])
class WeatherBatchConfigurationTest{
    @Autowired
    @Qualifier(WeatherBatchConfiguration.WEATHER_COLLECT_JOB_NAME)
    lateinit var sut: Job

    @Autowired
    lateinit var weatherAddressRepository: WeatherAddressRepository

    @Autowired
    lateinit var jobLauncherTestUtils: JobLauncherTestUtils


    @Test
    fun testJob() {

//        GatheringTargetSource.items.map {
//            WeatherAddress().apply {
//                this.waAddress = it.address
//                this.waX = it.x
//                this.waY = it.y
//            }
//        }.run {
//            weatherAddressRepository.saveAllAndFlush(this)
//        }


        // Given

        // When
//        val jobLauncherTestUtils= JobLauncherTestUtils()
        jobLauncherTestUtils.job = sut
        val execution = jobLauncherTestUtils.launchJob()

        // Then
        Assertions.assertThat(execution.status).isEqualTo(BatchStatus.COMPLETED)
    }
}