package hello.config;

import java.net.URISyntaxException;
import java.util.HashMap;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
//@ConfigurationProperties
@EnableJpaRepositories(
		basePackages = "hello.repository.mysql",
		entityManagerFactoryRef = "mysqlFactoryBean",
		transactionManagerRef = "mysqlTransactionManager"
)
public class MysqlConfig {
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	
	@Autowired
	private Environment env;
	
	@Bean
	public DataSource mysqlDataSource()
	{
	    DriverManagerDataSource dataSource = new DriverManagerDataSource();
	    dataSource.setDriverClassName(env.getProperty("spring.mysql.datasource.driver-class-name"));
	    dataSource.setUrl(env.getProperty("spring.mysql.datasource.url"));
	    dataSource.setUsername(env.getProperty("spring.mysql.datasource.username"));
	    dataSource.setPassword(env.getProperty("spring.mysql.datasource.password"));
		logger.debug("mysqlDataSource");
		return dataSource;
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean mysqlFactoryBean()
			throws URISyntaxException
	{
		LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
	    entityManagerFactoryBean.setDataSource( mysqlDataSource());
	    entityManagerFactoryBean.setPackagesToScan("hello.domain.mysql");
	    //additional config of factory

		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		entityManagerFactoryBean.setJpaVendorAdapter(vendorAdapter);
		entityManagerFactoryBean.setPersistenceUnitName("mysqlJPA");
		
		HashMap<String, Object> properties = new HashMap<String, Object>();
		properties.put("hibernate.show_sql", env.getProperty("mysql.jpa.show-sql"));
		properties.put("hibernate.dialect", env.getProperty("mysql.jpa.properties.hibernate.dialect"));
		properties.put("hibernate.ddl-auto", env.getProperty("mysql.jpa.hibernate.ddl-auto"));
		properties.put("hibernate.naming-strategy", env.getProperty("mysql.jpa.hibernate.naming-starategy"));
		
		entityManagerFactoryBean.setJpaPropertyMap(properties);
		
	    return entityManagerFactoryBean;
	}

	@Bean
	PlatformTransactionManager mysqlTransactionManager(@Qualifier("mysqlFactoryBean") EntityManagerFactory entityManagerFactory)
			throws URISyntaxException
	{
		logger.debug("getTransactionManager");
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory);
		return transactionManager;
	}
}
