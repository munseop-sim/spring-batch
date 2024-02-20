package spring.ms2709.batch.test1.batch

import org.springframework.batch.item.ItemProcessor
import org.slf4j.LoggerFactory
import spring.ms2709.batch.test1.intrastructure.entity.Person

/**
 * @class Test1PersonItemProcessor
 * @author 심문섭
 * @version 1.0
 * @since 2024-02-19 오전 11:14
 * @modified
 */

/**
 * ItemProcessor<I, O>
 * ItemProcessor의 첫번째 제네릭타입은 Input, 두번째 타입은 Output
 */
class Test1PersonItemProcessor : ItemProcessor<Person, Person> {
    companion object {
        private val log = LoggerFactory.getLogger(Test1PersonItemProcessor::class.java)
    }

    override fun process(person: Person): Person {
        val transformedPerson = Person().apply{
            this.firstName = person.firstName?.uppercase()
            this.lastName =  person.lastName?.uppercase()
        }
        log.info("Converting ($person) into ($transformedPerson)")
        return transformedPerson
    }
}