package spring.ms2709.batch.global.infrastructure.config

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling

/**
 * @class BatchConfiguration
 * @author 심문섭
 * @version 1.0
 * @since 2024-02-20 오전 9:21
 * @modified
 */
@EnableBatchProcessing
@EnableScheduling
@Configuration
class BatchConfiguration {
}