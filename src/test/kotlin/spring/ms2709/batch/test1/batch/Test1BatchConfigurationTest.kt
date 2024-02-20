package spring.ms2709.batch.test1.batch


import org.assertj.core.api.Assertions.assertThat
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
import spring.ms2709.batch.global.infrastructure.config.BatchConfiguration
import spring.ms2709.batch.global.infrastructure.config.datasource.MetaDataSourceConfig
import spring.ms2709.batch.global.infrastructure.config.datasource.Test1DataSourceConfig

/**
 *
 * Test1BatchConfiguration 에 선언된 job 테스트 클래스
 *
 * @class Test1BatchConfigurationTest
 * @author 심문섭
 * @version 1.0
 * @since 2024-02-20 오전 9:28
 * @modified
 */
@ComponentScan(basePackages = ["spring.ms2709.batch.test1"])
@EnableAutoConfiguration
@SpringBatchTest
@SpringBootTest(classes = [BatchConfiguration::class, Test1DataSourceConfig::class, MetaDataSourceConfig::class])
class Test1BatchConfigurationTest{

    @Autowired
    @Qualifier(Test1BatchConfiguration.TEST1_JOB_NAME)
    lateinit var sut: Job

    @Autowired
    lateinit var jobLauncherTestUtils:JobLauncherTestUtils

    @Test
    fun testJob() {
        // Given

        // When
//        val jobLauncherTestUtils= JobLauncherTestUtils()
        jobLauncherTestUtils.job = sut
        val execution = jobLauncherTestUtils.launchJob()

        // Then
        assertThat(execution.status).isEqualTo(BatchStatus.COMPLETED)
    }
}