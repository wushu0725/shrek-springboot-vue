package com.shrek.example.datasource;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

@Configuration
@MapperScan(basePackages = {"com.shrek.example.dao.mysql"},sqlSessionFactoryRef = "mysqlsqlSessionFactory")
public class MyBatisMysqlAConfig {

	@Autowired
    @Qualifier("mysqlDataSource")
    private DataSource dataSource;
	
	@Bean
    public SqlSessionFactory mysqlsqlSessionFactory() throws Exception{
        SqlSessionFactoryBean sqlSessionFactoryBean=new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
 
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resource=resolver.getResources("classpath:mapper/mysql/*.xml");
        sqlSessionFactoryBean.setMapperLocations(resource);
 
        return sqlSessionFactoryBean.getObject();
    }
	
	@Bean
    public SqlSessionTemplate mysqlsqlSession() throws Exception{
        SqlSessionTemplate sqlSessionTemplate=new SqlSessionTemplate(mysqlsqlSessionFactory());
 
        return sqlSessionTemplate;
    }
}
