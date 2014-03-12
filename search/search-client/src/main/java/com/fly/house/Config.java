package com.fly.house;

import com.google.common.eventbus.EventBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by dimon on 3/9/14.
 */
@Configuration
@ComponentScan("com.fly.house")
@PropertySource("classpath:application.properties")
public class Config {

    @Autowired
    private Environment env;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public EventBus eventBus() {
        return new EventBus();
    }

    @Bean
    public ThreadPoolTaskExecutor executor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(25);
        return executor;
    }

    @Bean
    public Path pathsListFile() {
        String paths = env.getProperty("paths");
        return Paths.get(paths);
    }

    @Bean
    public Path cookiePath() {
        String cookiePath = env.getProperty("cookiePath");
        return Paths.get(cookiePath);
    }
}