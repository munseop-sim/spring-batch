package spring.ms2709.batch.weather.batch

import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParameters
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import spring.ms2709.batch.global.extension.yyyyMMddWithTime
import java.time.LocalDateTime

/**
 * @class WeatherCollectJobRunner
 * @author 심문섭
 * @version 1.0
 * @since 2024-02-21 오후 1:15
 * @modified
 */
@Component
class WeatherCollectJobRunner (
    @Qualifier(WeatherCollectJobConfiguration.WEATHER_COLLECT_JOB_NAME) private val job: Job,
    private val jobLauncher: JobLauncher
){
    @Scheduled(cron = "0 30 * * * *")
    fun runJob(){
        val params: JobParameters = JobParametersBuilder()
            .addString("JobID", LocalDateTime.now().yyyyMMddWithTime())
            .toJobParameters()
        jobLauncher.run(job, params)
    }
}