package spring.ms2709.batch.test1.batch


/**
 * @class Test1JobCompletionNotificationListener
 * @author 심문섭
 * @version 1.0
 * @since 2024-02-19 오전 11:06
 * @modified
 */

import com.querydsl.jpa.impl.JPAQueryFactory
import org.slf4j.LoggerFactory
import org.springframework.batch.core.BatchStatus
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.JobExecutionListener
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import spring.ms2709.batch.global.infrastructure.config.datasource.Test1DataSourceConfig
import spring.ms2709.batch.test1.intrastructure.entity.QPerson


@Component
class Test1JobCompletionNotificationListener(
    @Qualifier(Test1DataSourceConfig.TEST1_JPA_QUERY_FACTORY) private val jpaQueryFactory: JPAQueryFactory
) : JobExecutionListener
     {
    private val log = LoggerFactory.getLogger(Test1JobCompletionNotificationListener::class.java)

    override fun afterJob(jobExecution: JobExecution) {
        if (jobExecution.status == BatchStatus.COMPLETED) {
            log.info("!!! JOB FINISHED! Time to verify the results")

            jpaQueryFactory.selectFrom(QPerson.person).fetch().forEach {
                    person -> log.info("Found <$person> in the database.")
            }
        }
    }
}