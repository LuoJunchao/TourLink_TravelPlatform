package org.tourlink.dataplatformservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class DataPlatformServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataPlatformServiceApplication.class, args);
    }

}
