package com.ycy.druid.config;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * Druid配置
 *
 * @author dudu
 * @date 2017-12-11 0:00
 */
@Configuration
public class DruidConfig {
    private Logger logger = LoggerFactory.getLogger(DruidConfig.class);

//    @Value("${spring.datasource.url:#{null}}")
//    private String dbUrl;
//    @Value("${spring.datasource.username: #{null}}")
//    private String username;
//    @Value("${spring.datasource.password:#{null}}")
//    private String password;
//    @Value("${spring.datasource.driverClassName:#{null}}")
//    private String driverClassName;
//    @Value("${spring.druid.initial-size:#{null}}")
//    private Integer initialSize;
//    @Value("${spring.druid.min-idle:#{null}}")
//    private Integer minIdle;
//    @Value("${spring.druid.maxActive:#{null}}")
//    private Integer maxActive;
//    @Value("${spring.druid.maxWait:#{null}}")
//    private Integer maxWait;
//    @Value("${spring.druid.timeBetweenEvictionRunsMillis:#{null}}")
//    private Integer timeBetweenEvictionRunsMillis;
//    @Value("${spring.druid.minEvictableIdleTimeMillis:#{null}}")
//    private Integer minEvictableIdleTimeMillis;
//    @Value("${spring.druid.validationQuery:#{null}}")
//    private String validationQuery;
//    @Value("${spring.druid.testWhileIdle:#{null}}")
//    private Boolean testWhileIdle;
//    @Value("${spring.druid.testOnBorrow:#{null}}")
//    private Boolean testOnBorrow;
//    @Value("${spring.druid.testOnReturn:#{null}}")
//    private Boolean testOnReturn;
//    @Value("${spring.druid.poolPreparedStatements:#{null}}")
//    private Boolean poolPreparedStatements;
//    @Value("${spring.druid.maxPoolPreparedStatementPerConnectionSize:#{null}}")
//    private Integer maxPoolPreparedStatementPerConnectionSize;
//    @Value("${spring.druid.filters:#{null}}")
//    private String filters;
//    @Value("{spring.druid.connectionProperties:#{null}}")
//    private String connectionProperties;

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DruidDataSource druidDataSource() {
        DruidDataSource druidDataSource = new DruidDataSource();
        List<Filter> filters = new ArrayList<>();
        filters.add(statFilter());
        filters.add(wallFilter());

        druidDataSource.setProxyFilters(filters);
        return druidDataSource;
    }

    @Bean
    public ServletRegistrationBean druidServlet() {
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");

        //控制台管理用户，加入下面2行 进入druid后台就需要登录
        servletRegistrationBean.addInitParameter("loginUsername", "admin");
        servletRegistrationBean.addInitParameter("loginPassword", "admin");
        return servletRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new WebStatFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        filterRegistrationBean.addInitParameter("profileEnable", "true");
        return filterRegistrationBean;
    }

    @Bean
    public StatFilter statFilter() {
        StatFilter statFilter = new StatFilter();
        statFilter.setLogSlowSql(true); //slowSqlMillis用来配置SQL慢的标准，执行时间超过slowSqlMillis的就是慢。
        statFilter.setMergeSql(true); //SQL合并配置
        statFilter.setSlowSqlMillis(1000);//slowSqlMillis的缺省值为3000，也就是3秒。
        return statFilter;
    }

    @Bean
    public WallFilter wallFilter() {
        WallFilter wallFilter = new WallFilter();
        //允许执行多条SQL
        WallConfig config = new WallConfig();
        config.setMultiStatementAllow(true);
        wallFilter.setConfig(config);
        return wallFilter;
    }
}
