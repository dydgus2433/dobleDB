package hello.config;

import java.net.URISyntaxException;
import java.util.HashMap;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
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
		basePackages = "hello.repository.h2",
		entityManagerFactoryRef = "h2FactoryBean",
		transactionManagerRef = "h2TransactionManager"
)
public class H2Config {

	Logger logger = LoggerFactory.getLogger(getClass());
	
	
	@Autowired
	private Environment env;
	
	@Primary
	@Bean
	public DataSource h2DataSource() {
		DriverManagerDataSource dataSource =  new DriverManagerDataSource();
		
		dataSource.setDriverClassName(env.getProperty("spring.h2.datasource.driver-class-name"));
	    dataSource.setUrl(env.getProperty("spring.h2.datasource.url"));
	    dataSource.setUsername(env.getProperty("spring.h2.datasource.username"));
	    dataSource.setPassword(env.getProperty("spring.h2.datasource.password"));
		
		return dataSource;
	}
	
	@Primary
	@Bean
	public LocalContainerEntityManagerFactoryBean h2FactoryBean()
			throws URISyntaxException
	{
		LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
	    entityManagerFactoryBean.setDataSource( h2DataSource());
	    entityManagerFactoryBean.setPackagesToScan("hello.domain.h2");
	    //additional config of factory

		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		entityManagerFactoryBean.setJpaVendorAdapter(vendorAdapter);
		
		//TODO: 중복준거 삭제해야함
		entityManagerFactoryBean.setPersistenceUnitName("h2JPA");


		HashMap<String, Object> properties = new HashMap<String, Object>();
		properties.put("hibernate.show_sql", env.getProperty("h2.jpa.show-sql"));
		properties.put("hibernate.dialect", env.getProperty("h2.jpa.properties.hibernate.dialect"));
		properties.put("hibernate.hbm2ddl.auto", env.getProperty("h2.jpa.hibernate.ddl-auto"));
		properties.put("hibernate.naming-strategy", env.getProperty("h2.jpa.hibernate.naming-starategy"));
		
		entityManagerFactoryBean.setJpaPropertyMap(properties);
		
	    return entityManagerFactoryBean;
	}
	@Primary
	@Bean
	PlatformTransactionManager h2TransactionManager(@Qualifier("h2FactoryBean") EntityManagerFactory entityManagerFactory)
			throws URISyntaxException
	{
		logger.debug("getTransactionManager");
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory);
		return transactionManager;
	}
}
