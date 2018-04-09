package es.uji.sape;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@SuppressWarnings("DesignForExtension")
public class SapeConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    public @NotNull DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

}
