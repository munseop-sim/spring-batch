package spring.ms2709.batch.test1.batch

import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParameters
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

/**
 *
 * 클래스 설명
 *
 * 사용 예 :
 * <pre>
 *   클래스 사용 예제
 *
 * </pre>
 *
 * @class Test1RunJob
 * @author 심문섭
 * @version 1.0
 * @since 2024-02-19 오후 2:37
 * @modified
 */
@Component
class Test1RunJob(
    @Qualifier(Test1BatchConfiguration.TEST1_JOB_NAME) private val job: Job,
    private val jobLauncher: JobLauncher
) {
    @Scheduled(cron = "0 0/1 * * * ?")
    fun runJob(){
        val params: JobParameters = JobParametersBuilder()
            .addString("JobID", System.currentTimeMillis().toString())
            .toJobParameters()
        jobLauncher.run(job, params)
    }
}