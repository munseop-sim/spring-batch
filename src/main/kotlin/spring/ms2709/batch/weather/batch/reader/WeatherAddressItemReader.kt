package spring.ms2709.batch.weather.batch.reader

import org.springframework.batch.item.ItemReader
import spring.ms2709.batch.weather.infrastructure.entity.WeatherAddress
import spring.ms2709.batch.weather.infrastructure.repository.WeatherAddressRepository

/**
 *
 * 클래스 설명
 *
 * @class WeatherAddressItemReader
 * @author 심문섭
 * @version 1.0
 * @since 2024-02-20 오후 11:19
 * @modified
 */
class WeatherAddressItemReader (
    weatherAddressRepository: WeatherAddressRepository
): ItemReader<WeatherAddress> {

    private val itemList:List<WeatherAddress> = weatherAddressRepository.findAll()
    private var index = 0

    override fun read(): WeatherAddress? {
        return if(index < itemList.size){
            itemList[index++]
        }else{
            null
        }
    }
}