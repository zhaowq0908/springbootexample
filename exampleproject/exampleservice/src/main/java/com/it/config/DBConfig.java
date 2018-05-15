package com.it.config;

import com.it.config.base.AbstractDruidDBConfig;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @author zhaowq
 * @description 数据源配置
 * @create 2017-08-25 上午 10:30
 **/
@Configuration
@EnableTransactionManagement
public class DBConfig extends AbstractDruidDBConfig {
    /**
     * 数据库连接url
     */
    @Value("${spring.datasource.url}")
    private String url;
    /**
     * 数据库用户名
     */
    @Value("${spring.datasource.username}")
    private String userName;
    /**
     * 数据库密码
     */
    @Value("${spring.datasource.password}")
    private String password;

    // 注册testDatasource
    @Bean(name = "testDatasource", initMethod = "init", destroyMethod = "close")
    public DataSource testDatasource() {
        return super.createDataSource(url, userName, password);
    }

    @Bean(name = "testSqlSessionFactory")
    public SqlSessionFactory testSqlSessionFactory(
            @Qualifier("testDatasource") DataSource testDatasource) throws Exception {
        return super.sqlSessionFactory(testDatasource);
    }

    @Bean
    public PlatformTransactionManager ecejServiceTransactionManager(
            @Qualifier("testDatasource") DataSource testDatasource) throws SQLException {
        return new DataSourceTransactionManager(testDatasource);
    }
}
