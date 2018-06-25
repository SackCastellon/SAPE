package es.uji.sape

import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.spring.SpringTransactionManager
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.annotation.EnableTransactionManagement
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import javax.sql.DataSource


@SpringBootApplication
class Application

@Configuration
@EnableTransactionManagement
class WebConfig : WebMvcConfigurer {
    @Bean
    @ConfigurationProperties("spring.datasource.hikari")
    fun dataSource(): DataSource = DataSourceBuilder.create().build()

    @Bean
    fun transactionManager(dataSource: HikariDataSource) = SpringTransactionManager(dataSource)
}

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
