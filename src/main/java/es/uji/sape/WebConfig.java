package es.uji.sape;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;

@Configuration
@SuppressWarnings("DesignForExtension")
public class WebConfig implements WebMvcConfigurer {

    @Bean
    @ConfigurationProperties("spring.datasource.hikari")
    public @NotNull DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Override
    public final void addResourceHandlers(@NotNull ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/webjars/**").addResourceLocations("/webjars/");
    }
}
