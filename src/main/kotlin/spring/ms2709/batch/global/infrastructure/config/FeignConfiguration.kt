package spring.ms2709.batch.global.infrastructure.config

import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Configuration

/**
 *
 * 클래스 설명
 *
 * @class FeignConfiguration
 * @author 심문섭
 * @version 1.0
 * @since 2024-02-20 오후 2:39
 * @modified
 */
@EnableFeignClients(basePackages = ["spring.ms2709.batch.external"])
@Configuration
class FeignConfiguration {
}