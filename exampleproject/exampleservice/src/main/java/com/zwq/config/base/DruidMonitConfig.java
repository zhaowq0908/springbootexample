package com.zwq.config.base;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.alibaba.druid.support.spring.stat.DruidStatInterceptor;
import org.springframework.aop.Advisor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.JdkRegexpMethodPointcut;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * @author zhaowq
 * @description 监控
 * @create 2017-12-21 下午 03:41
 **/
@EnableConfigurationProperties({DruidDbProperties.class})
@EnableAspectJAutoProxy(
        proxyTargetClass = true
)
public class DruidMonitConfig {
    @Resource
    private DruidDbProperties druidDbProperties;

    public DruidMonitConfig() {
    }

    @Bean
    public ServletRegistrationBean druidServlet() {
        ServletRegistrationBean reg = new ServletRegistrationBean();
        reg.setServlet(new StatViewServlet());
        reg.addUrlMappings(new String[]{"/druid/*"});
        if (!StringUtils.isEmpty(this.druidDbProperties.getAllow())) {
            reg.addInitParameter("allow", this.druidDbProperties.getAllow());
        }

        if (!StringUtils.isEmpty(this.druidDbProperties.getDeny())) {
            reg.addInitParameter("deny", this.druidDbProperties.getDeny());
        }

        reg.addInitParameter("loginUsername", this.druidDbProperties.getUsername());
        reg.addInitParameter("loginPassword", this.druidDbProperties.getPassword());
        return reg;
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new WebStatFilter());
        filterRegistrationBean.addUrlPatterns(new String[]{"/*"});
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        return filterRegistrationBean;
    }

    @Bean
    public DruidStatInterceptor druidStatInterceptor() {
        return new DruidStatInterceptor();
    }

    @Bean
    public JdkRegexpMethodPointcut druidStatPointcut() {
        JdkRegexpMethodPointcut druidStatPointcut = new JdkRegexpMethodPointcut();
        String patterns = "com.it.*";
        druidStatPointcut.setPatterns(new String[]{patterns});
        return druidStatPointcut;
    }

    @Bean
    public Advisor druidStatAdvisor() {
        return new DefaultPointcutAdvisor(this.druidStatPointcut(), this.druidStatInterceptor());
    }
}
