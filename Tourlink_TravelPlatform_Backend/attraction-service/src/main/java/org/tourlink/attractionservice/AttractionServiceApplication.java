package org.tourlink.attractionservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@SpringBootApplication
@RefreshScope
public class AttractionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AttractionServiceApplication.class, args);
    }

}
