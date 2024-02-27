package spring.ms2709.batch.global.service

import org.junit.jupiter.api.Assertions.*



import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.test.context.ActiveProfiles
import spring.ms2709.batch.RootApplicationTest
import spring.ms2709.batch.global.infrastructure.delegate.LogDelegate
import java.util.*
import kotlin.random.Random

/**
 *
 * redis hash 자료형 테스트
 *
 * hash자료형
 * 관계형 DB에서 하나의 pk와 하나이상의 컬럼으로 구성된 테이블 구조와 유사
 * 하나의 Key(pk)는오브젝트명과 하나이상의 필드값을 콜론 기호로 결합
 *
 * 저장되는 map의 key순서는 보장받지 못함
 *
 * 회사:서비스명:업무도메인:(pk_value or etc...)
 * -> a2d:yesus:customer:loginId 와  같은 구조로 저장
 *
 * redis 명령어
 *
 * hset - hash타입 단일 저장
 * hmset - hash타입 멀티 저장
 * hget - hash타입 조회(필드조회) :: hget Key field -> Key = 해당 키(db 기준pk), filed(db 기준 column)
 * hgetall hash타입 전체 조회
 * hkey - hash타입에 저장된 key조회
 * hlen - hash에 저장된 길이 조회
 *
 * del - 데이터 삭제
 *
 * @class RedisHashTest
 * @author 심문섭
 * @version 1.0
 * @since 2022-12-08 오전 10:11
 * @modified
 */
class RedisHashTest (
    @Autowired private val redisTemplate:RedisTemplate<String, String>,
    @Autowired private val redisService: RedisService
): RootApplicationTest(){

    private val logger by LogDelegate()
    private lateinit var redisKey:String


    @BeforeEach
    fun setRedisTempKey(){
        this.redisKey = UUID.randomUUID().toString().replace("-","")
    }

    @AfterEach
    fun onAfterEach(){
        logger.info("rediskKey->${redisKey}")
    }


    @DisplayName("명령어 테스트 (hset,hmset) ")
    @Test
    fun hSetCommandTest(){
        val opHash = redisTemplate.opsForHash<String,String>()

        // hset command
        opHash.put(redisKey, "hello-text", "안녕하세요.")

        val map  = mutableMapOf<String,String>()
        for(i in 1..10){
            map["${i}num"] = Random.nextInt().toString()
        }


        // hmset command
        opHash.putAll(redisKey, map)
        logger.info("mapKey->${redisKey}")

        redisTemplate.delete(redisKey)
    }

    @DisplayName("util - 명령어 테스트 (hset,hmset) ")
    @Test
    fun utilHSetCommandTest(){
        redisService.setMapValue(redisKey,"hello-text", "안녕하세요." )
        // hset command

        val map  = mutableMapOf<String,String>()
        for(i in 1..10){
            map["${i}num"] = Random.nextInt().toString()
        }


        // hmset command
        redisService.setMap(redisKey, map)

        redisService.deleteMapValue(redisKey, "1num")
        assertThat(redisService.getMap(redisKey).size).isEqualTo(10)

        redisService.delete(redisKey)
    }

    @Disabled("테스트코드 - 동작 확인용")
    @DisplayName("명령어 테스트 (hget,hgetall) ")
    @Test
    fun hGetCommandTest(){
        //redis에 "a2d:test:redis:hash"의 키로 3개의 key,value값이 있다고 가정한다.
        //
        // redis command
        // hset a2d:test:redis  'message' '테스트 조회용도입니다.' 'hello-text' '안녕하세요' 'bye-text' '안녕히계세요'

        val redisHashReadKey = "a2d:test:redis:hash"
        val opHash = redisTemplate.opsForHash<String,String>()

        val mapValue = opHash.get(redisHashReadKey,"hello-text")
        assertThat(mapValue).isNotNull.isEqualTo("안녕하세요")

        val map = opHash.entries(redisHashReadKey)
        assertThat(map.size).isEqualTo(3)
    }

    @Disabled("동작확인용")
    @DisplayName("util - 명령어 테스트 (hget,hgetall) ")
    @Test
    fun utilHGetCommandTest(){
        //redis에 "a2d:test:redis:hash"의 키로 3개의 key,value값이 있다고 가정한다.
        //
        // redis command
        // hset a2d:test:redis  'message' '테스트 조회용도입니다.' 'hello-text' '안녕하세요' 'bye-text' '안녕히계세요'

        val redisHashReadKey = "a2d:test:redis:hash"


        val mapValue =  redisService.getMapValue(redisHashReadKey,"hello-text")
        assertThat(mapValue).isNotNull.isEqualTo("안녕하세요")

        val map =  redisService.getMap(redisHashReadKey)
        assertThat(map).isNotNull
        assertThat(map.size).isEqualTo(3)
    }

    @DisplayName("hash에 데이터가 없을떄?")
    @Test
    fun getEmptyKey(){
        val opHash = redisTemplate.opsForHash<String,String>()
        val tempRedisKey = UUID.randomUUID().toString().replace("-","")

        val map = opHash.entries(tempRedisKey)
        assertThat(map).isNotNull
        assertThat(map.size).isEqualTo(0)

        val mapValue = opHash.get(tempRedisKey, "test")
        assertThat(mapValue).isNull()
    }
}
