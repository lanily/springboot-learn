package com.hsiao.springboot.transaction.config;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: JDBCConfig
 * @description: TODO
 * @author xiao
 * @create 2021/2/27
 * @since 1.0.0
 */
@Order(1)
@Data
@Configuration
@ConfigurationProperties(prefix = "jdbc")
@PropertySource("classpath:jdbc.properties")
public class DataSourceConfig {

//    @Value("${jdbc.url}")
    private String url;
//    @Value("${jdbc.driverClass}")
    private String driverClass;
//    @Value("${jdbc.username}")
    private String username;
//    @Value("${jdbc.password}")
    private String password;

//    @Value("${spring.datasource.url}")
//    private String url;
//
//    @Value("${spring.datasource.username}")
//    private String user;
//
//    @Value("${spring.datasource.password}")
//    private String password;

//    @Value("${spring.datasource.driver-class-name}")
//    String driverClassName;

    /**
     * Bean注解：该注解只能写在方法上，表明使用此方法创建一个对象，并且放入spring容器。
     * name属性：给当前@Bean注解方法创建的对象指定一个名称(即bean的id）。
     * @return
     */
    @Bean(name="dataSource")
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url); //数据源url
        config.setDriverClassName(driverClass); // driver class
        config.setUsername(username); //用户名
        config.setPassword(password); //密码
        config.addDataSourceProperty("cachePrepStmts", "true"); //是否自定义配置，为true时下面两个参数才生效
        config.addDataSourceProperty("prepStmtCacheSize", "250"); //连接池大小默认25，官方推荐250-500
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048"); //单条语句最大长度默认256，官方推荐2048
        config.addDataSourceProperty("useServerPrepStmts", "true"); //新版本MySQL支持服务器端准备，开启能够得到显著性能提升
        config.addDataSourceProperty("useLocalSessionState", "true");
        config.addDataSourceProperty("useLocalTransactionState", "true");
        config.addDataSourceProperty("rewriteBatchedStatements", "true");
        config.addDataSourceProperty("cacheResultSetMetadata", "true");
        config.addDataSourceProperty("cacheServerConfiguration", "true");
        config.addDataSourceProperty("elideSetAutoCommits", "true");
        config.addDataSourceProperty("maintainTimeStats", "false");

        HikariDataSource ds = new HikariDataSource(config);
        return ds;
    }
}

