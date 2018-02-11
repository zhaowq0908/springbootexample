package com.zwq.run;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author: zhaowq
 * @description: web启动入口
 * @create: 2018-02-11 10:57
 **/
@SpringBootApplication
@ComponentScan("com.zwq")
@EnableAutoConfiguration
public class StartRun {
    public static void main(String[] args) {
        SpringApplication.run(StartRun.class, args);
    }
}
