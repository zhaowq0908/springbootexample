package com.it.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = {"classpath:remote-dubbo.properties", "classpath:remote-db.properties"})
@ImportResource({"classpath:dubbo/*.xml"})
public class PropertiesConfig {
}
