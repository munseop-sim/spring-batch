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
 * redis sortedSet 자료형 테스트
 *
 *  sortedSet 자료형
 *  set타입과 유사하나 저장된 데이터가 정렬되어 있다. (순서 보장)
 *  redis에 저장되는 내용은 field, scroe로 이루어져 있다.
 *  score대로 정렬되어 있음
 *  zero base 로 score가 저장됨
 *
 *
 *  redis명령어
 *  zadd - redisKey에 sorted Set 자료형 추가
 *  zmembers - redisKey로 저장된 sortedSet자료형 조회
 *  zcard - redisKey로 저장된 sortedSet자료형의 size 조회
 *  zcount - 범위(순서)내 항목 조회
 *  zrank -
 *  zrevrank -
 *
 * @class RedisSetTest
 * @author 심문섭
 * @version 1.0
 * @since 2022-12-08 오후 10:22
 * @modified
 */
class RedisSortedSetTest(
    @Autowired private val redisTemplate: RedisTemplate<String, String>,
    @Autowired private val redisService: RedisService
): RootApplicationTest() {
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

    @DisplayName("zrank, zrevrank 테스트")
    @Test
    fun rankTest(){
        var opSet = redisTemplate.opsForZSet()
        for(i in 1..10){
            opSet.add(redisKey, "value=${i}" , (10-i).toDouble())
        }
        assertThat(opSet.rank(redisKey, "value=1")).isEqualTo(9L)
        assertThat(opSet.reverseRank(redisKey, "value=1")).isEqualTo(0L)


        assertThat(opSet.reverseRank(redisKey, "value=10")).isEqualTo(9L)
        assertThat(opSet.rank(redisKey, "value=10")).isEqualTo(0L)


        redisTemplate.delete(redisKey)
    }

    @Test
    @DisplayName("redisUtil - zrank, zrevrank 테스트")
    fun utilRankTest(){
        for(i in 1..10){
            redisService.addSortedSet(redisKey, "value=${i}" , (10-i))
        }

        assertThat(redisService.getRank(redisKey, "value=1")).isEqualTo(9L)
        assertThat(redisService.getReverseRank(redisKey, "value=1")).isEqualTo(0L)


        assertThat(redisService.getReverseRank(redisKey, "value=10")).isEqualTo(9L)
        assertThat(redisService.getRank(redisKey, "value=10")).isEqualTo(0L)


        redisService.delete(redisKey)
    }



    @DisplayName("zcount 테스트 범위 점수(score)내 항목 조회")
    @Test
    fun zCountTest(){
        var opSet = redisTemplate.opsForZSet()
        for(i in 1..10){
            opSet.add(redisKey, "value=${i}" , (10-i).toDouble())
        }



        val countResult = opSet?.count(redisKey,0.toDouble(),3.toDouble()) ?: return
        assertThat(countResult).isEqualTo(4L)

        redisTemplate.delete(redisKey)
    }

    @DisplayName("redisUtil - zcount 테스트 범위 점수(score)내 항목 조회")
    @Test
    fun utilZCountTest(){
        for(i in 1..10){
            redisService.addSortedSet(redisKey, "value=${i}" , (10-i))
        }
        val countResult = redisService.getRankCount(redisKey,0,3) ?: return
        assertThat(countResult).isEqualTo(4L)
        redisTemplate.delete(redisKey)
    }


    @DisplayName("상위 N개 조회")
    @Test
    fun zMembersTest(){

        val valueList  = listOf(
            Pair("1,2,3", 1),
            Pair("2,4,5", 2),
            Pair("3,4,5", 3)
        )
        redisService.addAllSortedSet(
            key = redisKey,
            values = valueList
        )

        val top1 = redisService.getTopRankValues(redisKey, 0) ?: return

        assertThat(top1.first()).isEqualTo(valueList[0].first)

        redisTemplate.delete(redisKey)
    }
}
