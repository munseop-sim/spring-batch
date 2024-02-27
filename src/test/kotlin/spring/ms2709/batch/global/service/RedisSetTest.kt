package spring.ms2709.batch.global.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.test.context.ActiveProfiles
import spring.ms2709.batch.RootApplicationTest
import spring.ms2709.batch.global.infrastructure.delegate.LogDelegate
import java.util.*

/**
 *
 * redis set 자료형 테스트
 *
 *  set 자료형
 *  - list 타입은 하나의 필드에 여러개의 배열 값을 저장할 수 있는 데이터 구조라면 Set타입은 배열구조가 아닌 여러개의 엘리먼트로 데이터 값을 표현하는 구조
 *  - list는 중복을 허용하나, set은 중복된 데이터가 들어갈 수 없음
 *  - set은 중복을 허용하지 않으나, 저장순서는 정해져 있지 않다.
 *
 *
 *  redis명령어
 *  sadd - redisKey에 set 자료형 추가
 *  smembers - redisKey로 저장된 set자료형 조회
 *  scard - redisKey로 저장된 set자료형의 size 조회
 *  sdiff - 인자를 2개를 받는데, sdiff set1 set2 --> set1 에만 있는 value 출력
 *  sdiffstore - sdiffstore set1 set2 set3 -> set2, set3의 교집합의 결과를 set1에 저장
 *  sunion - sunion set1 set2 -> set1, set2 합집합 (중복제거)
 *  sunionstore - sunion set1 set2 set3 -> set2, set3의 union 결과를 set1에 저장
 *  srem - srem set1 value -> set1에 있는 value제거
 *
 * @class RedisSetTest
 * @author 심문섭
 * @version 1.0
 * @since 2022-12-08 오후 6:22
 * @modified
 */

class RedisSetTest(
    @Autowired private val redisTemplate: RedisTemplate<String, String>,
    @Autowired private val redisService: RedisService
) : RootApplicationTest(){
    private val logger by LogDelegate()
    private lateinit var  redisKey:String
    @BeforeEach
    fun setRedisTempKey(){
        this.redisKey = UUID.randomUUID().toString().replace("-","")
    }


    @AfterEach
    fun onAfterEach(){
        logger.info("redisKey -> $redisKey")
    }


    @DisplayName("sadd 테스트")
    @Test
    fun saddTest(){
        var opSet = redisTemplate.opsForSet()
        opSet.add(redisKey, "1,2,3","2,4,5","3,4,5")

        redisTemplate.delete(redisKey)
    }


    @DisplayName("util - sadd 테스트")
    @Test
    fun utilSaddTest(){
        redisService.addSetType(redisKey, "1,2,3")
        redisService.addAllSetType(redisKey, listOf("1,2,3","2,4,5","3,4,5"))

        redisTemplate.delete(redisKey)
    }

    @DisplayName("members 테스트 출력결과 = ${"1,2,3"}::${"2,4,5"}::${"3,4,5"}")
    @Test
    fun sMembersTest(){
        var opSet = redisTemplate.opsForSet()
        opSet.add(redisKey, "1,2,3","2,4,5","3,4,5")

        val values = opSet.members(redisKey)

        logger.info(values?.joinToString("::") ?: throw Exception("redis members error"))
        redisTemplate.delete(redisKey)
    }

    @DisplayName("util- members 테스트 출력결과 = ${"1,2,3"}::${"2,4,5"}::${"3,4,5"}")
    @Test
    fun utilSMembersTest(){

        val values = listOf("1,2,3","2,4,5","3,4,5")
        redisService.addAllSetType(redisKey,values)

        val selectedValues = redisService.getSetTypeValues(redisKey)

        logger.info(selectedValues?.joinToString("::") ?: throw Exception("redis members error"))
        redisTemplate.delete(redisKey)
    }

    @DisplayName("scard 테스트")
    @Test
    fun sCardTest(){
        var opSet = redisTemplate.opsForSet()
        opSet.add(redisKey, "1,2,3","2,4,5","3,4,5")

        assertThat(opSet.size(redisKey)).isNotNull.isEqualTo(3L)

        redisTemplate.delete(redisKey)
    }

    @DisplayName("util- scard 테스트")
    @Test
    fun utilSCardTest(){
        val values = listOf("1,2,3","2,4,5","3,4,5")
        redisService.addAllSetType(redisKey,values)

        assertThat(redisService.getSetTypeSize(redisKey)).isNotNull.isEqualTo(values.size.toLong())

        redisTemplate.delete(redisKey)
    }


    @DisplayName("sUnion (합집합) 테스트")
    @Test
    fun sUnionTest(){
        var opSet = redisTemplate.opsForSet()
        opSet.add(redisKey, "1,2,3","2,4,5","3,4,5")
        val redisKey2 = UUID.randomUUID().toString().replace("-","")
        opSet.add(redisKey2, "3,4,5", "7","8")

        val unionValue = opSet.union(redisKey, redisKey2)
        assertThat(unionValue).isNotNull
        assertThat(unionValue?.size).isEqualTo(5)
        assertThat(unionValue).isEqualTo(mutableSetOf("1,2,3","2,4,5","3,4,5", "7","8"))

        redisTemplate.delete(redisKey)
        redisTemplate.delete(redisKey2)
    }


    @DisplayName("util - sUnion (합집합) 테스트")
    @Test
    fun utilSUnionTest(){
        val values1 = listOf("1,2,3","2,4,5","3,4,5")
        val values2 = listOf("3,4,5", "7","8")
        redisService.addAllSetType(redisKey, values1)
        val redisKey2 = UUID.randomUUID().toString().replace("-","")
        redisService.addAllSetType(redisKey2, values2)



        val unionValue = redisService.unionSetTypes(redisKey, redisKey2)
        assertThat(unionValue).isNotNull
        assertThat(unionValue?.size).isEqualTo(5)
        assertThat(unionValue).isEqualTo(mutableSetOf("1,2,3","2,4,5","3,4,5", "7","8"))

        redisTemplate.delete(redisKey)
        redisTemplate.delete(redisKey2)
    }

    @DisplayName("sRem 테스트 - element 제거 ")
    @Test
    fun sRemTest(){
        var opSet = redisTemplate.opsForSet()
        opSet.add(redisKey, "1,2,3","2,4,5","3,4,5")

        opSet.remove(redisKey, "1,2,3")

        logger.info(opSet.members(redisKey)?.joinToString("::"))
        redisTemplate.delete(redisKey)

    }

    @DisplayName("util - sRem 테스트 - element 제거 ")
    @Test
    fun utilSRemTest(){

        val values = listOf("1,2,3","2,4,5","3,4,5")
        redisService.addAllSetType(redisKey, values)

        redisService.removeSetTypeElement(redisKey, values[0])

        logger.info(redisService.getSetTypeValues(redisKey)?.joinToString("::"))
        assertThat(redisService.getSetTypeSize(redisKey)).isEqualTo(2)
        redisTemplate.delete(redisKey)
    }
}
