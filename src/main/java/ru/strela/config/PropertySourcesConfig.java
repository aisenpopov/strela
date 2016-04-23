package ru.strela.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

@Configuration
public class PropertySourcesConfig {
    public static final String DEFAULT = "webapp-default.properties";
    public static final String TEST = "webapp-test.properties";

    @Profile("default")
    @PropertySource("classpath:" + DEFAULT)
    public static class DevConfig {
        @Bean
        public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
            PropertySourcesPlaceholderConfigurer pspc = new PropertySourcesPlaceholderConfigurer();
            pspc.setLocations(new Resource[] {new ClassPathResource(DEFAULT)});
            return pspc;
        }
    }

    @Profile("test")
    @PropertySource("classpath:" + TEST)
    public static class TestConfig {
        @Bean
        public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
            PropertySourcesPlaceholderConfigurer pspc = new PropertySourcesPlaceholderConfigurer();
            pspc.setLocations(new Resource[]{new ClassPathResource(TEST)});
            return pspc;
        }
    }
}
