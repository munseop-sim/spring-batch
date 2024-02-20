package spring.ms2709.batch.test1.batch


/**
 * @class Test1JobCompletionNotificationListener
 * @author 심문섭
 * @version 1.0
 * @since 2024-02-19 오전 11:06
 * @modified
 */

import org.slf4j.LoggerFactory
import org.springframework.batch.core.BatchStatus
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.JobExecutionListener
import org.springframework.stereotype.Component
import spring.ms2709.batch.test1.intrastructure.repository.PersonRepository


@Component
class Test1JobCompletionNotificationListener(
    private val personRepository: PersonRepository
) : JobExecutionListener
     {
    private val log = LoggerFactory.getLogger(Test1JobCompletionNotificationListener::class.java)

    override fun afterJob(jobExecution: JobExecution) {
        if (jobExecution.status == BatchStatus.COMPLETED) {
            log.info("!!! JOB FINISHED! Time to verify the results")

            personRepository.findAll().forEach {
                    person -> log.info("Found <$person> in the database.")
            }
        }
    }
}