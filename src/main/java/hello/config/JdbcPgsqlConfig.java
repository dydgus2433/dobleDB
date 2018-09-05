package hello.config;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

public class JdbcPgsqlConfig
{
	
	@Bean(name = "pgsqlDataSource")
	@ConfigurationProperties(prefix = "spring.conn_pgsql_test")
	public DataSource getDataSource()
	{
		return DataSourceBuilder.create().build();
	}

	@Bean(name = "pgsqlFactoryBean")
	public LocalContainerEntityManagerFactoryBean getFactoryBean(EntityManagerFactoryBuilder builder)
	{
		return builder.dataSource(getDataSource()).packages("com.web.domain.pgsql").build();
	}

	@Bean(name = "pgsqlTransactionManager")
	PlatformTransactionManager getTransactionManager(EntityManagerFactoryBuilder builder)
	{
		return new JpaTransactionManager(getFactoryBean(builder).getObject());
	}
}