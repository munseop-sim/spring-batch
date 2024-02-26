package spring.ms2709.batch.global.infrastructure.delegate

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 *
 * 로그생성 delegate
 *
 * @class LogDelegate
 * @author 심문섭
 * @version 1.0
 * @since 2024-02-21 오전 9:31
 * @modified
 */
class LogDelegate : ReadOnlyProperty<Any, Logger> {
    companion object {
        private fun <T>createLogger(clazz: Class<T>) : Logger {
            return LoggerFactory.getLogger(clazz)
        }
    }
    private var log:Logger? = null

    override fun getValue(thisRef: Any, property: KProperty<*>): Logger {
        if(log == null){
            log = createLogger(thisRef.javaClass)
        }
        return log!!
    }
}