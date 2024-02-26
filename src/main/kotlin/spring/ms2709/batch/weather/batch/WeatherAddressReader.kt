package spring.ms2709.batch.weather.batch

import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.ItemStream
import spring.ms2709.batch.global.infrastructure.delegate.LogDelegate
import spring.ms2709.batch.weather.infrastructure.entity.WeatherAddress
import spring.ms2709.batch.weather.infrastructure.repository.simple.WeatherAddressSimpleQuery

/**
 * @class WeatherAddressReader
 * @author 심문섭
 * @version 1.0
 * @since 2024-02-21 오후 4:22
 * @modified
 */
class WeatherAddressReader(
    private val weatherAddressSimpleQuery: WeatherAddressSimpleQuery,
    private val pageSize:Int,
):ItemReader<WeatherAddress>, ItemStream {
    private var index = 0
    private val log by LogDelegate()
    private var pageIndex = 0
    private var itemList:List<WeatherAddress> = listOf()

    override fun read(): WeatherAddress? {
        log.info("RUN ITEM READER --> index : ${index}")
        if(index>=itemList.size){
            itemList = weatherAddressSimpleQuery.getAll(pageSize,pageIndex)
            if(itemList.isEmpty()){
                return null
            }
            pageIndex++
            index = 0
        }
        return itemList[index++]
    }
    override fun close() {
        log.info("RUN ITEM READER --> close")
        pageIndex = 0
    }
}
