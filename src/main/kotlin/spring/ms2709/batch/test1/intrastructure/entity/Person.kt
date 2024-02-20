package spring.ms2709.batch.test1.intrastructure.entity

import jakarta.persistence.*
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

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
 * @class Person
 * @author 심문섭
 * @version 1.0
 * @since 2024-02-19 오전 11:13
 * @modified
 */
@DynamicUpdate
@DynamicInsert
@EntityListeners(AuditingEntityListener::class)
@Table(name = "people")
@Entity
class Person{
    @Id
    @Column(name="person_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var personId:Long? = null

    @Column(name = "person_first_name")
    var firstName:String? = null

    @Column(name = "person_last_name")
    var lastName:String? = null

    @CreatedDate
    var createdTime:LocalDateTime? = null

    override fun toString(): String {
        return "Person(personId=$personId, firstName=$firstName, lastName=$lastName)"
    }


}