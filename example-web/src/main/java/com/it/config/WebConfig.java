package com.it.config;

import com.it.interceptor.WebInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author: zhaowq
 * @description: web配置
 * @create: 2018-02-11 11:06
 **/
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {
    /**
     * 在容器启动时注册拦截器前将拦截器初始化
     * 在Spring添加拦截器之前先自己创建一下这个Spring Bean，这样就能在Spring映射这个拦截器前，把拦截器中的依赖注入给完成了。
     *
     * @return
     */
    /*方式一*/
    @Bean
    public WebInterceptor webInterceptor() {
        return new WebInterceptor();
    }

    /**
     * 方式二
     *
     * @Autowired private WebInterceptor webInterceptor;
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(webInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/sessionError")
                .excludePathPatterns("/error");
    }
}
