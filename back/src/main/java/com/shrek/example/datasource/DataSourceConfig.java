package com.shrek.example.datasource;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * 多数据源配置
 * @author 吴署
 *
 */

@Configuration
public class DataSourceConfig {
	
	
	@Primary
    @Bean(name="mysqlDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.mysql")
    public DataSource readDataSource(){
        return DataSourceBuilder.create().build();
    }
	

    @Bean(name="paibanDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.sqlservice")
    public DataSource paibanDataSource(){
        return DataSourceBuilder.create().build();
    }
    
    @Bean(name="repairDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.sqlservice2")
    public DataSource repairDataSource(){
        return DataSourceBuilder.create().build();
    }
    
    @Bean(name="basedataDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.sqlservice3")
    public DataSource basedataDataSource(){
        return DataSourceBuilder.create().build();
    }
	
}
