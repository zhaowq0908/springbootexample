package com.it.config.base;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.github.pagehelper.PageHelper;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author zhaowq
 * @description 数据源配置基类
 * @create 2017-12-21 下午 03:39
 **/
@Configuration
@EnableConfigurationProperties({DruidDbProperties.class})
@Import({DruidMonitConfig.class})
public abstract class AbstractDruidDBConfig {
    private Logger logger = LoggerFactory.getLogger(AbstractDruidDBConfig.class);
    @Resource
    private DruidDbProperties druidDbProperties;

    public AbstractDruidDBConfig() {
    }

    public DruidDataSource createDataSource(String url, String username, String password) {
        if (StringUtils.isEmpty(url)) {
            this.logger.error("Your database connection pool configuration is incorrect! Please check your Spring profile! spring.datasource.url  is null");
            throw new ApplicationContextException("Database connection pool is not configured correctly! spring.datasource.url is null");
        } else {
            DruidDataSource datasource = new DruidDataSource();
            datasource.setUrl(url);
            datasource.setUsername(username);
            datasource.setPassword(password);
            datasource.setDriverClassName(this.druidDbProperties.getDriverClassName());
            datasource.setInitialSize(this.druidDbProperties.getInitialSize());
            datasource.setMinIdle(this.druidDbProperties.getMinIdle());
            datasource.setMaxActive(this.druidDbProperties.getMaxActive());
            datasource.setMaxWait((long) this.druidDbProperties.getMaxWait());
            datasource.setTimeBetweenEvictionRunsMillis((long) this.druidDbProperties.getTimeBetweenEvictionRunsMillis());
            datasource.setMinEvictableIdleTimeMillis((long) this.druidDbProperties.getMinEvictableIdleTimeMillis());
            datasource.setValidationQuery(this.druidDbProperties.getValidationQuery());
            datasource.setTestWhileIdle(this.druidDbProperties.isTestWhileIdle());
            datasource.setTestOnBorrow(this.druidDbProperties.isTestOnBorrow());
            datasource.setTestOnReturn(this.druidDbProperties.isTestOnReturn());

            try {
                datasource.setFilters(this.druidDbProperties.getFilters());
            } catch (SQLException var6) {
                this.logger.error("druid configuration initialization filter", var6);
            }

            datasource.setConnectionProperties(this.druidDbProperties.getConnectionProperties());
            return datasource;
        }
    }

    public DruidDataSource createDataSource(String url, String username, String password, String driverClass) {
        DruidDataSource datasource = this.createDataSource(url, username, password);
        if (StringUtils.isNotEmpty(driverClass)) {
            datasource.setDriverClassName(driverClass);
        }
        return datasource;
    }

    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        return this.createSqlSessionFactory(dataSource, "classpath:mybatis/**/*.xml");
    }

    public SqlSessionFactory sqlSessionFactory(DataSource dataSource, String mapperLocations) throws Exception {
        return this.createSqlSessionFactory(dataSource, mapperLocations);
    }

    private SqlSessionFactory createSqlSessionFactory(DataSource dataSource, String mapperLocations) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        PageHelper pageHelper = new PageHelper();
        Properties props = new Properties();
        props.setProperty("dialect", "mysql");
        props.setProperty("reasonable", "true");
        props.setProperty("supportMethodsArguments", "true");
        props.setProperty("returnPageInfo", "check");
        props.setProperty("params", "count=countSql");
        pageHelper.setProperties(props);
        sqlSessionFactoryBean.setPlugins(new Interceptor[]{pageHelper});
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources(mapperLocations));
        return sqlSessionFactoryBean.getObject();
    }
}
