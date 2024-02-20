package spring.ms2709.batch.global.infrastructure.config.datasource

import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaDialect
import org.springframework.transaction.PlatformTransactionManager
import javax.sql.DataSource

/**
 *
 * 클래스 설명
 *
 * @class WeatherDataSourceConfig
 * @author 심문섭
 * @version 1.0
 * @since 2024-02-20 오후 3:31
 * @modified
 */
@Configuration
@EnableJpaRepositories(
    basePackages = ["spring.ms2709.batch.weather"],
    entityManagerFactoryRef = WeatherDataSourceConfig.WEATHER_ENTITY_MANAGER_FACTORY,
    transactionManagerRef = WeatherDataSourceConfig.WEATHER_DATASOURCE_TRANSACTION_MANAGER
)
class WeatherDataSourceConfig (
    private val jpaProperties: JpaProperties,
    private val hibernateProperties: HibernateProperties
){

    @Value("\${data.weather-datasource.init-script}") // 위치를 지정한 SQL 스크립트
    lateinit var initScript: String

    @Bean(WEATHER_DATASOURCE)
    @ConfigurationProperties(prefix = "data.weather-datasource")
    fun weatherDataSource(): DataSource {
        val datasource = DataSourceBuilder.create().build()
        return datasource
    }


    @Bean(WEATHER_ENTITY_MANAGER_FACTORY)
    fun weatherEntityManagerFactory(builder: EntityManagerFactoryBuilder): LocalContainerEntityManagerFactoryBean {
        val properties = hibernateProperties.determineHibernateProperties(
            jpaProperties.properties,
            HibernateSettings()
        )
        properties["hibernate.hbm2ddl.auto"] = "create"
        properties["hibernate.dialect"] = "org.hibernate.dialect.H2Dialect"
        properties["hibernate.hbm2ddl.import_files"] = "/${initScript}"
        return builder.dataSource(weatherDataSource())
            .properties(properties)
            .packages("spring.ms2709.batch.weather")
            .persistenceUnit(WEATHER_JPA_UNIT_NAME)
            .build()
    }

    @Bean(name=[WEATHER_JPA_TRANSACTION_MANAGER])
    fun weatherTractionManager(
        @Qualifier(WEATHER_ENTITY_MANAGER_FACTORY) entityManagerFactory: LocalContainerEntityManagerFactoryBean
    ): JpaTransactionManager {
        //JpaTransactionManager는 Spring의 JPA를 지원하는 트랜잭션 매니저
        return JpaTransactionManager().apply {
            this.entityManagerFactory = entityManagerFactory.`object`
            this.setJpaDialect(HibernateJpaDialect())
        }
    }



    @Bean(name=[WEATHER_DATASOURCE_TRANSACTION_MANAGER])
    fun weatherTransactionManager(): PlatformTransactionManager {
        //DataSourceTransactionManager는 Spring의 JDBC를 지원하는 트랜잭션 매니저
        return DataSourceTransactionManager(weatherDataSource())
    }


    @Bean(name=[WEATHER_JDBC_TEMPLATE])
    fun weatherJdbcTemplate(): NamedParameterJdbcTemplate {
        return NamedParameterJdbcTemplate(weatherDataSource())
    }

    @Bean(name=[WEATHER_JPA_QUERY_FACTORY])
    fun weatherJpaQueryFactory(
        @Qualifier(WEATHER_ENTITY_MANAGER_FACTORY)entityManager: EntityManager
    ): JPAQueryFactory {
        return JPAQueryFactory(entityManager)
    }



    companion object{
        const val WEATHER_DATASOURCE_TRANSACTION_MANAGER = "weatherDataSourceTransactionManager"
        const val WEATHER_JDBC_TEMPLATE = "weatherJdbcTemplate"
        const val WEATHER_DATASOURCE = "weatherDataSource"

        const val WEATHER_ENTITY_MANAGER_FACTORY = "weatherEntityManagerFactory"
        const val WEATHER_JPA_TRANSACTION_MANAGER = "weatherJpaTransactionManager"
        const val WEATHER_JPA_QUERY_FACTORY="weatherJpaQueryFactory"
        private const val WEATHER_JPA_UNIT_NAME = "weatherMaster"
    }
}