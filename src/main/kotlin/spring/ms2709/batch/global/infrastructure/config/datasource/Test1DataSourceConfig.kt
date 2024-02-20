package spring.ms2709.batch.global.infrastructure.config.datasource

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaDialect
import org.springframework.transaction.PlatformTransactionManager
import javax.sql.DataSource

/**
 * @class Test1DataSourceConfig
 * @author 심문섭
 * @version 1.0
 * @since 2024-02-19 오전 9:21
 * @modified
 */
@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(
    basePackages = ["spring.ms2709.batch.test1"],
    entityManagerFactoryRef = Test1DataSourceConfig.TEST1_ENTITY_MANAGER_FACTORY,
    transactionManagerRef = Test1DataSourceConfig.TEST1_DATASOURCE_TRANSACTION_MANAGER
)
class Test1DataSourceConfig(
    private val jpaProperties: JpaProperties,
    private val hibernateProperties: HibernateProperties
) {
    @Bean(TEST1_DATASOURCE)
    @ConfigurationProperties(prefix = "data.test1-datasource")
    fun test1DataSource(): DataSource {
        return DataSourceBuilder.create().build()
    }


    @Bean(TEST1_ENTITY_MANAGER_FACTORY)
    fun test1EntityManagerFactory(builder: EntityManagerFactoryBuilder): LocalContainerEntityManagerFactoryBean {
        val properties = hibernateProperties.determineHibernateProperties(
            jpaProperties.properties,
            HibernateSettings()
        )
        return builder.dataSource(test1DataSource())
            .properties(properties)
            .packages("spring.ms2709.batch.test1")
            .persistenceUnit("test1Master")
            .build()
    }

    @Bean(TEST1_JPA_TRANSACTION_MANAGER)
    fun test1TractionManager(
        @Qualifier(TEST1_ENTITY_MANAGER_FACTORY) entityManagerFactory: LocalContainerEntityManagerFactoryBean
    ):JpaTransactionManager{
        //JpaTransactionManager는 Spring의 JPA를 지원하는 트랜잭션 매니저
        return JpaTransactionManager().apply {
            this.entityManagerFactory = entityManagerFactory.`object`
            this.setJpaDialect(HibernateJpaDialect())
        }
    }



    @Bean(TEST1_DATASOURCE_TRANSACTION_MANAGER)
    fun test1TransactionManager(): PlatformTransactionManager {
        //DataSourceTransactionManager는 Spring의 JDBC를 지원하는 트랜잭션 매니저
        return DataSourceTransactionManager(test1DataSource())
    }


    @Bean(TEST1_JDBC_TEMPLATE)
    fun test1JdbcTemplate():NamedParameterJdbcTemplate{
        return NamedParameterJdbcTemplate(test1DataSource())
    }

    companion object{
        const val TEST1_ENTITY_MANAGER_FACTORY = "test1EntityManagerFactory"
        const val TEST1_JPA_TRANSACTION_MANAGER = "test1JpaTransactionManager"
        const val TEST1_DATASOURCE_TRANSACTION_MANAGER = "test1DataSourceTransactionManager"
        const val TEST1_JDBC_TEMPLATE = "test1JdbcTemplate"
        const val TEST1_DATASOURCE = "test1DataSource"
    }
}