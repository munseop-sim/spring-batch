package spring.ms2709.batch.global.infrastructure.config.datasource

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.transaction.PlatformTransactionManager
import javax.sql.DataSource

/**
 * @class MetaDataSourceConfig
 * @author 심문섭
 * @version 1.0
 * @since 2024-02-19 오전 10:47
 * @modified
 */
@Configuration
class MetaDataSourceConfig{

    @Primary
    @Bean(MEA_DATASOURCE)
    @ConfigurationProperties(prefix = "data.meta-datasource")
    fun metaDatasource(): DataSource {
        return DataSourceBuilder.create().build()
    }

    @Primary
    @Bean(META_TRANSACTION_MANAGER)
    fun test1TransactionManager(): PlatformTransactionManager {
        return DataSourceTransactionManager(metaDatasource())
    }

    @Primary
    @Bean(META_JDBC_TEMPLATE)
    fun test1JdbcTemplate(): NamedParameterJdbcTemplate {
        return NamedParameterJdbcTemplate(metaDatasource())
    }

    companion object{
        const val MEA_DATASOURCE = "dataSource"
        const val META_TRANSACTION_MANAGER = "transactionManager"
        const val META_JDBC_TEMPLATE = "metaJdbcTemplate"
    }
}