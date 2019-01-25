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

//@Configuration
//@MapperScan(basePackages = {"com.shrek.example.dao.paiban"},sqlSessionFactoryRef = "paibansqlSessionFactory")
public class MyBatisPaibanAConfig {

	@Autowired
    @Qualifier("paibanDataSource")
    private DataSource dataSource;
	
	@Bean
    public SqlSessionFactory paibansqlSessionFactory() throws Exception{
        SqlSessionFactoryBean sqlSessionFactoryBean=new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
 
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resource=resolver.getResources("classpath:mapper/paiban/*.xml");
        sqlSessionFactoryBean.setMapperLocations(resource);
 
        return sqlSessionFactoryBean.getObject();
    }
	
	@Bean
    public SqlSessionTemplate paibansqlSession() throws Exception{
        SqlSessionTemplate sqlSessionTemplate=new SqlSessionTemplate(paibansqlSessionFactory());
 
        return sqlSessionTemplate;
    }
}
