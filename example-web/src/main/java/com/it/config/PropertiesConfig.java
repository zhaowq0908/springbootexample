package com.it.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;

/**
 * @author: zhaowq
 * @description: properties相关配置
 * @create: 2018-02-11 11:10
 **/
@Configuration
@PropertySource(value = {"classpath:remote-dubbo.properties"})
@ImportResource({"classpath:dubbo/*.xml"})
public class PropertiesConfig {
}
