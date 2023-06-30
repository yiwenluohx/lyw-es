package com.study.elastic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author luohx
 */
@SpringBootApplication(scanBasePackages = {"com.study.elastic"})
public class ElasticApplyApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElasticApplyApplication.class, args);
    }

}
