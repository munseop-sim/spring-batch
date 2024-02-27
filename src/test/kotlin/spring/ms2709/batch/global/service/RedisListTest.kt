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
 * redis List 자료형 테스트
 *
 * list자료형
 * list타입은 기존의 관계형 테이블에는 존재하지 않는 데이터 유형이다. 개발언어에서의 배열과 유사한 구조
 * 'redisKey = []' 쯤으로 이해하면 될 것 같다.
 * 기본적으로 string 타입의 경우 배열에 저장할 수 있는 크기는 512mb (각 row의 크기??)
 *
 * 스택/큐 자료구조 형태가 필요할 때 사용
 *
 * 중간에 추가/삭제가 느리다. 따라서 head-tail에서 추가/삭제 한다. (push / pop 연산)
 * 메세지 queue로 사용하기 적절하다.
 *
 *
 * redis 명령어
 *
 * lpush - 왼쪽에서 부터 데이터 삽입
 * lpop - 왼쪽에서 부터 데이터 추출. pop하게 되면 list에서 삭제됨
 * lrange - 인덱스 범위 조회
 * rpush - 오른쪽에서 부터 데이터 삽입
 * rpop - 오른족에서 부터 데이터 추출, list에서 데이터 삭제
 * llen - 길이 조회
 * lindex - 해당 인덱스의 데이터 조회
 *
 *
 * @class RedisListTest
 * @author 심문섭
 * @version 1.0
 * @since 2022-12-08 오후 3:37
 * @modified
 */

class RedisListTest (
    @Autowired private val redisTemplate: RedisTemplate<String, String>,
    @Autowired private val redisService: RedisService
): RootApplicationTest(){

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

    @DisplayName("lpush, lpop 테스트 스택과 유사")
    @Test
    fun leftTest(){
        val opList = redisTemplate.opsForList()

        val minVal = 1
        val maxVal = 10

        //왼쪽부터 데이터가 들어가기 때문에 입력된 데이터를 확인해보면 10,9,8...형태로 입력되어 있음
        for(i in minVal..maxVal){
            opList.leftPush(redisKey, i.toString() )
        }

        for(i in minVal..maxVal){
            assertThat(opList.leftPop(redisKey)?.toInt() ?: null).isNotNull.isNotEqualTo(i).isEqualTo(maxVal - i +1)
        }
        assertThat(opList.leftPop(redisKey)).isNull()
        assertThat(opList.rightPop(redisKey)).isNull()

        redisTemplate.delete(redisKey)
    }

    @DisplayName("util - lpush, lpop 테스트 스택과 유사")
    @Test
    fun utilLeftTest(){
        val minVal = 1
        val maxVal = 10

        //왼쪽부터 데이터가 들어가기 때문에 입력된 데이터를 확인해보면 10,9,8...형태로 입력되어 있음
        for(i in minVal..maxVal){
            redisService.leftPush(redisKey, "$i")
        }

        for(i in minVal..maxVal){
            assertThat(redisService.leftPop(redisKey) ?: null).isNotNull.isNotEqualTo("$i").isEqualTo("${maxVal - i +1}")
        }
        assertThat(redisService.leftPop(redisKey)).isNull()
        assertThat(redisService.rightPop(redisKey)).isNull()

        redisService.delete(redisKey)
    }

    @DisplayName("rpush, rpop 테스트 스택과 유사")
    @Test
    fun rightTest(){
        val opList = redisTemplate.opsForList()

        val minVal = 1
        val maxVal = 10
        for(i in minVal..maxVal){
            opList.rightPush(redisKey, i.toString() )
        }
        for(i in minVal..maxVal){
            opList.rightPush(redisKey, i.toString() )
        }

        for(i in minVal..maxVal){
            val popValue = opList.rightPop(redisKey)?.toInt() ?: null
            logger.info("popValue -> $popValue")
            assertThat(popValue).isNotNull
            assertThat(popValue).isNotEqualTo(i)
            assertThat(popValue).isEqualTo(maxVal - i +1)
        }
        assertThat(opList.rightPop(redisKey)).isNotNull()
        redisTemplate.delete(redisKey)
    }

    @DisplayName("util - rpush, rpop 테스트 스택과 유사")
    @Test
    fun utilRightTest(){

        val minVal = 1
        val maxVal = 10

        //중복입력 여부 확인
        for(i in minVal..maxVal){
            redisService.rightPush(redisKey, i.toString() )
        }
        for(i in minVal..maxVal){
            redisService.rightPush(redisKey, i.toString() )
        }

        assertThat(redisService.getListSize(redisKey)).isNotNull.isEqualTo(20L)

        for(i in minVal..maxVal){
            assertThat(redisService.rightPop(redisKey) ?: null).isNotNull.isNotEqualTo("$i").isEqualTo("${maxVal - i +1}")
        }
        assertThat(redisService.rightPop(redisKey)).isNotNull

        redisService.delete(redisKey)
    }

    @DisplayName("rpush, lpop 테스트, 큐와 유사")
    @Test
    fun rightLeftTest(){
        val opList = redisTemplate.opsForList()

        val minVal = 1
        val maxVal = 10
        for(i in minVal..maxVal){
            opList.rightPush(redisKey, i.toString() )
        }
        for(i in minVal..maxVal){
            assertThat(opList.leftPop(redisKey)?.toInt() ?: null).isNotNull.isEqualTo(i)
        }
        assertThat(opList.rightPop(redisKey)).isNull()
        redisTemplate.delete(redisKey)
    }

    @DisplayName("util - rpush, lpop 테스트, 큐와 유사")
    @Test
    fun utilRightLeftTest(){
        val minVal = 1
        val maxVal = 10
        for(i in minVal..maxVal){
            redisService.rightPush(redisKey, i.toString() )
        }
        for(i in minVal..maxVal){
            assertThat(redisService.leftPop(redisKey)?.toInt() ?: null).isNotNull.isEqualTo(i)
        }
        assertThat(redisService.rightPop(redisKey)).isNull()
        redisService.delete(redisKey)
    }


    @DisplayName("range, index 테스트")
    @Test
    fun rangeTest(){
        val opList = redisTemplate.opsForList()
        val minVal = 1
        val maxVal = 10

        for(i in minVal..maxVal){
            opList.rightPush(redisKey, i.toString() )
        }

        var listVal = opList.range(redisKey, 0, 3) // 0,1,2,3 번째 인덱스에 위치한 데이터 조회
        assertThat(listVal?.size).isEqualTo(4)


        //전부 리턴
        assertThat(opList.range(redisKey, 0,-1)?.size).isEqualTo(maxVal)
        assertThat(opList.range(redisKey, 1,9999999999999)?.size).isEqualTo(maxVal-1)
        assertThat(redisService.getAllListValues(redisKey).size).isEqualTo(10)

        //start,end 잘못 기입
        assertThat(opList.range(redisKey, 100,1)?.size).isEqualTo(0)

        //첫번째 인덱스 위치 데이터 조회
        assertThat(opList.index(redisKey, 0)?.toInt() ?: null).isNotNull.isEqualTo(minVal)
        assertThat(redisService.getListFirstValue(redisKey)?.toInt()).isEqualTo(minVal).isEqualTo(opList.index(redisKey, 0)?.toInt() ?: null)

        //마지막 위치 인덱스 데이터 조회
        assertThat(opList.index(redisKey, -1)?.toInt() ?: null).isNotNull.isEqualTo(maxVal)
        assertThat(redisService.getListLastValue(redisKey)?.toInt()).isEqualTo(maxVal).isEqualTo(opList.index(redisKey, -1)?.toInt() ?: null)

        //삭제된 데이터가 있는지 확인하기 위함
        assertThat(opList.size(redisKey)).isEqualTo(maxVal.toLong())

        redisTemplate.delete(redisKey)
    }


}
