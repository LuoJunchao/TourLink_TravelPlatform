package org.tourlink.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.tourlink.common.config.WebMvcConfig;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "org.tourlink.userservice.repository")
@EnableFeignClients
@EnableDiscoveryClient
@Import(WebMvcConfig.class)
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

}
