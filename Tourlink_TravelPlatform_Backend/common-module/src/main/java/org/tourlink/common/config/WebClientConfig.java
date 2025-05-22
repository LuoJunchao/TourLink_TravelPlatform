package org.tourlink.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * WebClient和RestTemplate配置
 * 各微服务可以导入此配置类
 */
@Configuration
public class WebClientConfig {
    
    /**
     * 创建WebClient.Builder Bean
     * @return WebClient.Builder
     */
    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
    
    /**
     * 创建WebClient Bean
     * @param builder WebClient.Builder
     * @return WebClient
     */
    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        return builder.build();
    }
    
    /**
     * 创建RestTemplate Bean
     * @return RestTemplate
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
