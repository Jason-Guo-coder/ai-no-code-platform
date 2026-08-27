package com.gjq.ainocodeplatform;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.gjq.ainocodeplatform.mapper")
public class AiNoCodePlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiNoCodePlatformApplication.class, args);
    }

}
