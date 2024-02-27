package spring.ms2709.batch.global.service

import java.time.Duration

/**
 * @class RedisService
 * @author 심문섭
 * @version 1.0
 * @since 2024-02-23 오후 3:04
 * @modified
 */
interface RedisService {
    /**
     * String value set
     */
    fun set(key:String, value:String)

    fun set(key: String, value: String, timeout: Duration)

    /**
     * String value get
     */
    fun get(key:String):String?

    /**
     * String value get And Delete
     * (String 자료형을 pop처럼 사용하기 위함)
     */
    fun getAndDelete(key:String):String?

    /**
     * 공통 - delete key
     */
    fun delete(key:String):Boolean

    /**
     * 공통 - exists key check
     */
    fun hasKey(key:String):Boolean

    /**
     * list value type
     * lpop
     */
    fun rightPop(key:String):String?

    fun rightPopAll(key: String): List<String>

    /**
     * list value type
     * rpush
     */
    fun rightPush(key:String, value:String):Long?

    fun rightPushAll(key: String, vararg value: String):Long?
    fun rightPushAll(key: String, values: Collection<String>):Long?

    /**
     * list value type
     * lpop
     */
    fun leftPop(key:String):String?

    fun leftPopAll(key: String):List<String>

    /**
     * list value type
     * lpush
     */
    fun leftPush(key:String,value:String):Long?

    fun leftPushAll(key: String, vararg value: String):Long?

    fun leftPushAll(key: String, values: Collection<String>):Long?

    /**
     * list value type
     * get first
     */
    fun getListFirstValue(key:String):String?

    /**
     * list value type
     * get last
     */
    fun getListLastValue(key:String):String?

    fun getAllListValues(key:String):List<String>

    /**
     * list value type
     * get list value size
     */
    fun getListSize(key:String):Long?

    /**
     * hash value type
     */
    fun setMap(key:String, map:Map<String,String>)

    /**
     * hash value type
     */
    fun setMapValue(key:String, mapKey:String, mapValue:String)

    /**
     * hash value type
     */
    fun getMap(key:String) : Map<String,String>

    /**
     * hash value type
     */
    fun getMapValue(key:String,mapKey:String) :String?

    /**
     * hash value type
     */
    fun deleteMapValue(key:String, mapKey:String):Long?

    /**
     * set value type
     *
     * redis command - sadd
     */
    fun addSetType(key:String, value:String):Long?
    /**
     * set value type
     *
     * addAll
     */
    fun addAllSetType(key:String, values:List<String>)

    /**
     * set value type
     * redis command - members
     */
    fun getSetTypeValues(key:String):List<String>

    /**
     * set value type
     * redis command - scard
     */
    fun getSetTypeSize(key:String):Long?

    /**
     * ser value type
     * redis command - srem
     */
    fun removeSetTypeElement(key:String, value:String):Long?

    /**
     * set value  type
     * redis command - sunion
     */
    fun unionSetTypes(key1:String, key2:String):Set<String>

    /**
     * sortedSet value type
     *
     * redis command - zadd
     */
    fun addSortedSet(key:String, value:String, score:Int):Boolean?

    /**
     * sortedSet value type
     *
     * addAll
     */
    fun addAllSortedSet(key:String, values:List<Pair<String,Int>>)

    /**
     * sortedSet value type
     *
     * redis command - zrank
     */
    fun getRank(key:String,value:String):Long?

    /**
     * sortedSet value type
     *
     * redis command - zrevrank
     */
    fun getReverseRank(key:String,value:String):Long?

    /**
     * sortedSet value type
     *
     * redis command - zcount,
     * (zero base)
     */
    fun getRankCount(key:String, start:Int, end:Int):Long?

    /**
     * sortedSet value type
     *
     * 상위 count 만큼의 value 조회
     */
    fun getTopRankValues(key:String, count:Int):MutableSet<String>?
}
