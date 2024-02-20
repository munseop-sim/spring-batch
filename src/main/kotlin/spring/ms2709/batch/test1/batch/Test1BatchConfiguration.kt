package spring.ms2709.batch.test1.batch


/**
 * @class BatchConfiguration
 * @author 심문섭
 * @version 1.0
 * @since 2024-02-19 오전 11:12
 * @modified
 */

import org.slf4j.LoggerFactory
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.file.FlatFileItemReader
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.orm.jpa.JpaTransactionManager
import spring.ms2709.batch.global.infrastructure.config.datasource.Test1DataSourceConfig
import spring.ms2709.batch.test1.intrastructure.entity.Person
import spring.ms2709.batch.test1.intrastructure.repository.PersonRepository


@Configuration
class Test1BatchConfiguration(
    private val personRepository: PersonRepository
) {

    companion object{
        const val TEST1_JOB_NAME="importUserFromCsvJob"
    }


    private val log = LoggerFactory.getLogger(Test1BatchConfiguration::class.java)


    fun reader(): FlatFileItemReader<Person> = FlatFileItemReaderBuilder<Person>()
        .name("personItemReader")
        .resource(ClassPathResource("sample-data.csv"))
        .delimited()
        .names("firstName", "lastName")
        .targetType(Person::class.java)
        .build()

    fun writer(): ItemWriter<Person> {
        return ItemWriter {
            val saved =  personRepository.saveAll(it)
            saved.forEach{log.info(it.toString())}
            saved
        }
    }

//    @Bean("personItemWriter")
//    fun writer() : RepositoryItemWriter<Person> {
//        val builder = RepositoryItemWriterBuilder<Person>()
//
//        builder.repository(personRepository)
//        builder.methodName("save")
//        return builder.build()
////        val writer = RepositoryItemWriter<Person>().apply {
////            this.setRepository(personRepository)
////            this.setMethodName("save")
////        }
////
////        return writer
//    }

    @Bean(Test1BatchConfiguration.TEST1_JOB_NAME)
    fun importUserJob(listener: Test1JobCompletionNotificationListener, step1: Step, jobRepository: JobRepository): Job{
        val job =JobBuilder(Test1BatchConfiguration.TEST1_JOB_NAME, jobRepository)
            .listener(listener)
            .start(step1)
            .build()
        return job
    }

    @Bean
    fun step1(
        jobRepository: JobRepository,
        @Qualifier(Test1DataSourceConfig.TEST1_JPA_TRANSACTION_MANAGER) jpaTransactionManager: JpaTransactionManager
    ): Step{
//        Tasklet vs Chunk
//        Tasklet: 한 가지 이상의 CRUD가 발생(=비즈니스 로직)하는 task에 대해 일괄적으로 처리하는 경우, 채택한다. (복잡한 로직을 수행해야 하는 job일 경우, 채택)
//        Chunk: chunk 단위로 처리할 모든 record를 쭉 읽어들인 후, 모두 읽어들이는데 성공하면 한번에 Write하는 방식 (대용량 데이터에 대해 단순 처리할 경우, 채택)
        val step = StepBuilder("step1",jobRepository)
            .chunk<Person, Person>(2,jpaTransactionManager)
            .reader(reader())
            .processor(Test1PersonItemProcessor())
            .writer(writer())
            .build()
        return step
    }



}