package spring.ms2709.batch.global.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.test.context.ActiveProfiles
import spring.ms2709.batch.RootApplicationTest
import java.time.Duration
import java.util.*

/**
 *
 * redis string 자료형 테스트
 *  *
 * string 자료형
 * 하나의 value만을 갖는다.
 *
 * get - 조회
 * set - 설정
 * del - 삭제
 *
 *
 * @class RedisStringTest
 * @author 심문섭
 * @version 1.0
 * @since 2022-12-08 오후 10:46
 * @modified
 */
class RedisStringTest(
    @Autowired private val redisTemplate: RedisTemplate<String, String>,
    @Autowired private val redisService: RedisService
) : RootApplicationTest() {

    @DisplayName("string value get/set test")
    @Test
    fun test(){
        val redisKey = UUID.randomUUID().toString().replace("-","")
        val opValue = redisTemplate.opsForValue()
        val value = "hello"
        opValue.set(redisKey, value)
        assertThat(opValue.get(redisKey)).isEqualTo(value)
        redisTemplate.delete(redisKey)
    }

    @DisplayName("redisUtil - string value get/set test")
    @Test
    fun utilTest(){
        val redisKey = UUID.randomUUID().toString().replace("-","")

        val value = "hello"
        redisService.set(redisKey, value)

        assertThat(redisService.get(redisKey)).isEqualTo(value)
        redisService.delete(redisKey)

        redisService.set("hello", "hello-value", Duration.ofMinutes(2))
    }
}
