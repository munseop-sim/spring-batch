package spring.ms2709.batch.weather.batch

import org.slf4j.LoggerFactory
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.JobExecutionListener
import org.springframework.stereotype.Component

/**
 *
 * 클래스 설명
 *
 * @class WeatherCollectCompletedListener
 * @author 심문섭
 * @version 1.0
 * @since 2024-02-20 오후 3:54
 * @modified
 */
@Component
class WeatherCollectCompletedListener : JobExecutionListener {
    private val log = LoggerFactory.getLogger(WeatherCollectCompletedListener::class.java)
    override fun afterJob(jobExecution: JobExecution) {
        log.info("WEATHER COLLECTED")
    }
}