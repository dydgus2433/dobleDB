package hello.config;

import java.net.URISyntaxException;
import java.util.HashMap;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
//@EnableTransactionManagement
//@ConfigurationProperties
//@EnableJpaRepositories(
//		basePackages = "hello.repository.sqlite",
//		entityManagerFactoryRef = "sqliteFactoryBean",
//		transactionManagerRef = "sqliteTransactionManager"
//)
@MapperScan(value= "hello.dao.sqlite" ,
sqlSessionFactoryRef = "sqliteSqlSessionFactory")
public class SQLiteConfig {

	Logger logger = LoggerFactory.getLogger(getClass());
	
	
	@Autowired
	private Environment env;
	
	
	@Bean
	public DataSource sqliteDataSource() {
		DriverManagerDataSource dataSource =  new DriverManagerDataSource();
		dataSource.setDriverClassName("org.sqlite.JDBC");
		dataSource.setUrl("jdbc:sqlite:test.db");
		return dataSource;
	}
	
	@Bean
	public PlatformTransactionManager sqliteTransactionManager() {
		return new DataSourceTransactionManager(sqliteDataSource());
	}
	
	@Bean
	public SqlSessionFactory sqliteSqlSessionFactory(@Qualifier("sqliteDataSource") DataSource sqliteDataSource , 
			ApplicationContext applicationContext) throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(sqliteDataSource);
		sqlSessionFactoryBean.setTypeAliasesPackage("hello.domain.sqlite");
		sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:mapper/**/*.xml"));
		
		return sqlSessionFactoryBean.getObject();
	}
	

//	@Bean
//	public LocalContainerEntityManagerFactoryBean sqliteFactoryBean()
//			throws URISyntaxException
//	{
//		LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
//	    entityManagerFactoryBean.setDataSource( sqliteDataSource());
//	    entityManagerFactoryBean.setPackagesToScan("hello.domain.sqlite");
//	    //additional config of factory
//
//		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
//		entityManagerFactoryBean.setJpaVendorAdapter(vendorAdapter);
//		
//		//TODO: 중복준거 삭제해야함
//		entityManagerFactoryBean.setPersistenceUnitName("sqliteJPA");
//		
//		HashMap<String, Object> properties = new HashMap<String, Object>();
//		properties.put("hibernate.show_sql", env.getProperty("sqlite.jpa.show-sql"));
//		properties.put("hibernate.dialect", env.getProperty("sqlite.jpa.properties.hibernate.dialect"));
//		properties.put("hibernate.ddl-auto", env.getProperty("sqlite.jpa.hibernate.ddl-auto"));
//		properties.put("hibernate.naming-strategy", env.getProperty("sqlite.jpa.hibernate.naming-starategy"));
//		
//		entityManagerFactoryBean.setJpaPropertyMap(properties);
//		
//	    return entityManagerFactoryBean;
//	}
//
//	@Bean
//	PlatformTransactionManager sqliteTransactionManager(@Qualifier("sqliteFactoryBean") EntityManagerFactory entityManagerFactory)
//			throws URISyntaxException
//	{
//		logger.debug("getTransactionManager");
//		JpaTransactionManager transactionManager = new JpaTransactionManager();
//		transactionManager.setEntityManagerFactory(entityManagerFactory);
//		return transactionManager;
//	}
}
