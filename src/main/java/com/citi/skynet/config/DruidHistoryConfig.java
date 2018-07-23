package com.citi.skynet.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.google.common.collect.Lists;

@Configuration
@MapperScan(basePackages = DruidHistoryConfig.PACKAGE,sqlSessionFactoryRef = "historySqlSessionFactory")
public class DruidHistoryConfig {

	static final String PACKAGE = "com.citi.skynet.mapper.history";
	
	@ConfigurationProperties(prefix = "spring.druid.history")
	@Bean(name = "historyDataSource", initMethod = "init", destroyMethod = "close")
	@Primary
	public DataSource historyDataSource() throws Exception {
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

    @Bean
	@Primary
	public ServletRegistrationBean servletRegistrationBean() {
		return new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
	}
	
    //数据源事务管理器
    @Bean(name="historyDataSourceTransactionManager")
	@Primary
    public DataSourceTransactionManager historyDataSourceTransactionManager() throws Exception{
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(historyDataSource());
        return dataSourceTransactionManager;
    }

    //创建Session
    @Bean(name="historySqlSessionFactory")
	@Primary
    public SqlSessionFactory historySqlSessionFactory() throws Exception{
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(historyDataSource());
        return sqlSessionFactoryBean.getObject();
    }

}
