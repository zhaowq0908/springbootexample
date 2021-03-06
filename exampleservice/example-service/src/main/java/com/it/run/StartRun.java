package com.it.run;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author: zhaowq
 * @description: service启动入口
 * @create: 2018-02-11 14:39
 **/
@SpringBootApplication
@ComponentScan("com.it")
@EnableAutoConfiguration
public class StartRun {
    public static void main(String[] args) {
        SpringApplication.run(StartRun.class, args);
    }
}
