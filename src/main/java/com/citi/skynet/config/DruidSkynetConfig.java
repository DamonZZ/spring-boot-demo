package com.citi.skynet.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.google.common.collect.Lists;

@Configuration
@MapperScan(basePackages = DruidSkynetConfig.PACKAGE,sqlSessionFactoryRef = "skynetSqlSessionFactory")
public class DruidSkynetConfig {
	static final String PACKAGE = "com.citi.skynet.mapper.skynet";
	
	@ConfigurationProperties(prefix = "spring.druid.skynet")
	@Bean(name = "skynetDataSource", initMethod = "init", destroyMethod = "close")
	public DataSource skynetDataSource() throws Exception {
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setProxyFilters(Lists.newArrayList(statFilter()));
		return dataSource;
	}

	public Filter statFilter() {
		StatFilter filter = new StatFilter();
		filter.setSlowSqlMillis(1);
		filter.setLogSlowSql(true);
		filter.setMergeSql(true);
		return filter;
	}

	
    //数据源事务管理器
    @Bean(name="skynetDataSourceTransactionManager")
    public DataSourceTransactionManager activeDataSourceTransactionManager() throws Exception{
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(skynetDataSource());
        return dataSourceTransactionManager;
    }

    //创建Session
    @Bean(name="skynetSqlSessionFactory")
    public SqlSessionFactory activeSqlSessionFactory() throws Exception{
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(skynetDataSource());
        return sqlSessionFactoryBean.getObject();
    }
}
