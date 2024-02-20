package spring.ms2709.batch.test1.intrastructure.repository

import org.springframework.data.jpa.repository.JpaRepository
import spring.ms2709.batch.test1.intrastructure.entity.Person

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
 * @class PersonRepository
 * @author 심문섭
 * @version 1.0
 * @since 2024-02-19 오후 12:48
 * @modified
 */
interface PersonRepository : JpaRepository<Person, Long> {}
