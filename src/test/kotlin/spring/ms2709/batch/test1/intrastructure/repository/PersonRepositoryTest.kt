package spring.ms2709.batch.test1.intrastructure.repository

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.Commit
import org.springframework.transaction.annotation.Transactional
import spring.ms2709.batch.global.infrastructure.config.datasource.Test1DataSourceConfig
import spring.ms2709.batch.test1.intrastructure.entity.Person
import spring.ms2709.batch.test1.intrastructure.entity.QPerson

/**
 *
 * 클래스 설명
 *
 * 사용 예 :
 * <pre>
 * 클래스 사용 예제
 *
</pre> *
 *
 * @class PersonRepositoryTest
 * @author 심문섭
 * @version 1.0
 * @since 2024-02-19 오후 4:56
 * @modified
 */
@Transactional(transactionManager = Test1DataSourceConfig.TEST1_JPA_TRANSACTION_MANAGER)
@SpringBootTest
class PersonRepositoryTest @Autowired constructor(val sut: PersonRepository) {
    private val log  = LoggerFactory.getLogger(PersonRepository::class.java)

    @Commit
    @Test
    fun insertTest(){
        //given
        val person = Person().apply {
            this.firstName = "munseop"
            this.lastName ="sim"
        }


        //when
        val saved = sut.save(person)
        sut.flush()

        //then
        assertThat(saved).isNotNull
        log.info(saved.toString())
    }
}
