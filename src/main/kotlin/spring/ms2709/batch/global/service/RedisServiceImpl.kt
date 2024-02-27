package spring.ms2709.batch.global.service

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.time.Duration

/**
 * @class RedisServiceImpl
 * @author 심문섭
 * @version 1.0
 * @since 2024-02-23 오후 3:07
 * @modified
 */
@Component
class RedisServiceImpl (
    private val redisTemplate: RedisTemplate<String, String>
):RedisService{
    /**
     * string 자료형을 다루기 위한 연산 객체
     */
    private val valueOp = redisTemplate.opsForValue()

    /**
     * set 자료형을 다루기 위한 연산 객체
     * field타입은 String으로 제한
     */
    private val setOp = redisTemplate.opsForSet()

    /**
     * SortedSet 자료형을 다루기 위한 연산객체
     * field타입은 String으로 제한
     */
    private val zSetOp = redisTemplate.opsForZSet()

    /**
     * list 자료형을 다루기 위한 연산객체
     * field타입은 String으로 제한
     */
    private val listOp = redisTemplate.opsForList()

    /**
     * hash 자료형을 다루기 위한 연산객체
     * hash 타입을 <String,String>으로 제한
     */
    private val hashOp = redisTemplate.opsForHash<String,String>()

    override fun set(key: String, value: String) {
        valueOp.set(key, value)
    }

    override fun set(key: String, value: String, timeout: Duration) {
        valueOp.set(key, value, timeout)
    }

    override fun get(key: String): String? = valueOp.get(key)

    override fun getAndDelete(key: String): String? {
        val value = valueOp.get(key) ?: return null
        this.delete(key)
        return value
    }

    override fun delete(key: String): Boolean= redisTemplate.delete(key)

    override fun hasKey(key: String): Boolean = redisTemplate.hasKey(key)

    override fun rightPop(key: String): String? = listOp.rightPop(key)

    override fun rightPopAll(key: String): List<String> {
        val listSize = listOp.size(key)
        val result = listOp.rightPop(key, listSize?.toLong() ?: 0)

        return result ?: listOf()
    }

    override fun rightPush(key: String, value: String): Long? = listOp.rightPush(key,value)

    override fun rightPushAll(key: String, vararg value: String): Long? = listOp.rightPushAll(key, *value)

    override fun rightPushAll(key: String, values: Collection<String>):Long? =listOp.rightPushAll(key, values)

    override fun leftPop(key: String): String?= listOp.leftPop(key)

    override fun leftPopAll(key: String) : List<String>{
        val listSize = listOp.size(key)
        val result = listOp.leftPop(key, listSize?.toLong() ?: 0)

        return result ?: listOf()
    }

    override fun leftPush(key: String, value: String): Long?  = listOp.leftPush(key, value)

    override fun leftPushAll(key: String, vararg value: String):Long? = listOp.leftPushAll(key, *value)

    override fun leftPushAll(key: String, values: Collection<String>):Long? {
        return if(values.isEmpty()){
            null
        }else{
            listOp.leftPushAll(key, values)
        }
    }

    override fun getListFirstValue(key: String): String? =  listOp.index(key, 0)

    override fun getListLastValue(key: String): String?  =  listOp.index(key, -1)

    override fun getAllListValues(key: String): List<String>  = listOp.range(key, 0, -1)?.toList() ?: listOf()

    override fun getListSize(key: String): Long? =  listOp.size(key)

    override fun setMap(key: String, map: Map<String, String>) = hashOp.putAll(key, map)

    override fun setMapValue(key: String, mapKey: String, mapValue: String) = hashOp.put(key,mapKey,mapValue)

    override fun getMap(key: String): Map<String, String> = hashOp.entries(key)

    override fun getMapValue(key: String, mapKey: String): String? = hashOp.get(key,mapKey)


    override fun deleteMapValue(key: String, mapKey: String): Long? = hashOp.delete(key, mapKey)

    override fun addSetType(key: String, value: String): Long? = setOp.add(key, value)

    override fun addAllSetType(key: String, values: List<String>) {
        values.forEach {
            this.addSetType(key, it)
        }
    }

    override fun getSetTypeValues(key: String): List<String> = setOp.members(key)?.toList() ?: listOf()

    override fun getSetTypeSize(key: String): Long? = setOp.size(key)

    override fun removeSetTypeElement(key: String, value: String): Long? = setOp.remove(key, value)

    override fun unionSetTypes(key1: String, key2: String): Set<String> = setOp.union(key1, key2)?.toSet() ?: setOf()

    override fun addSortedSet(key: String, value: String, score: Int): Boolean? = zSetOp.add(key, value, score.toDouble())

    override fun addAllSortedSet(key: String, values: List<Pair<String, Int>>) {
        values.forEach { this.addSortedSet(key, it.first, it.second) }
    }

    override fun getRank(key: String, value: String): Long? = zSetOp.rank(key, value)

    override fun getReverseRank(key: String, value: String): Long? = zSetOp.reverseRank(key, value)

    override fun getRankCount(key: String, start: Int, end: Int): Long? =  zSetOp.count(key, start.toDouble(), end.toDouble())

    override fun getTopRankValues(key: String, count: Int): MutableSet<String>? = zSetOp.range(key,0L, count.toLong())
}
