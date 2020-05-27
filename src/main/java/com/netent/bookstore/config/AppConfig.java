package com.netent.bookstore.config;

import java.time.Duration;
import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;

import lombok.Getter;
import lombok.Setter;

/**
 * App Config for rest teemplate and executor
 * @author jgarg
 *
 */
@Configuration
@Getter
@Setter
public class AppConfig {
  
  
  private static final int CORE_POOL_SIZE = 5;

  private static final int MAX_POOL_SIZE = 5;

  private static final int QUEUE_CAPACITY = 5;


  @Value("${restTemp.connectionTimeout}")
  private String connnectionTimeout;
  
  @Value("${restTemp.readTimeout}")
  private String readTimeout;
  
  @Bean(name = "restTemplate")
  public RestTemplate restTemp(RestTemplateBuilder builder) {
    // customize restTemplate set connection timeout and read timeout
    return builder
        .setConnectTimeout(
            Duration.ofMillis(Long.parseLong(connnectionTimeout)))
        .setReadTimeout(
            Duration.ofMillis(Long.parseLong(readTimeout)))
        .build();
    
  }
  @Bean(name = "asyncTitleExecutor")
  public Executor asyncTitleExecutor() {
    ThreadPoolTaskExecutor executorTitle  = new ThreadPoolTaskExecutor();
    executorTitle.setCorePoolSize(CORE_POOL_SIZE);
    executorTitle.setMaxPoolSize(MAX_POOL_SIZE);
    executorTitle.setQueueCapacity(QUEUE_CAPACITY);
    executorTitle.initialize();
    return executorTitle;
  }
  
  @Bean(name = "asyncBodyExecutor")
  public Executor asyncBodyExecutor() {
    ThreadPoolTaskExecutor executorBody  = new ThreadPoolTaskExecutor();
    executorBody.setCorePoolSize(CORE_POOL_SIZE);
    executorBody.setMaxPoolSize(MAX_POOL_SIZE);
    executorBody.setQueueCapacity(QUEUE_CAPACITY);
    executorBody.initialize();
    return executorBody;
  }
  
  


}
