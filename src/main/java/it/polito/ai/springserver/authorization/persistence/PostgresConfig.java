package it.polito.ai.springserver.authorization.persistence;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;
import java.util.logging.Logger;

@Configuration
@EnableTransactionManagement
@PropertySource("classpath:authorization/postgres.properties")
@EntityScan("it.polito.ai.springserver.authorization.model")
@EnableJpaRepositories(basePackages = "it.polito.ai.springserver.authorization.model.repository")
public class PostgresConfig {

  @Autowired
  private Environment env;

  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory(){
    final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
    em.setDataSource(restDataSource());
    em.setPackagesToScan("it.polito.ai.springserver.authorization.model");
    final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    vendorAdapter.setDatabase(Database.POSTGRESQL);
    vendorAdapter.setGenerateDdl(true);

    em.setJpaVendorAdapter(vendorAdapter);
    em.setJpaProperties(additionalProperties());

    return em;
  }

  @Bean
  public DataSource restDataSource() {
    final BasicDataSource dataSource = new BasicDataSource();
    dataSource.setDriverClassName(env.getProperty("jdbc.driverClassName"));
    dataSource.setUrl(System.getProperty("jdbc.url", env.getProperty("jdbc.url")));
    dataSource.setUsername(System.getProperty("jdbc.username", env.getProperty("jdbc.username")));
    dataSource.setPassword(System.getProperty("jdbc.password", env.getProperty("jdbc.password")));
    dataSource.setMaxActive(Integer.valueOf(env.getProperty("jdbc.maxTotal")));
    dataSource.setMaxIdle(Integer.valueOf(env.getProperty("jdbc.maxIdle")));
    dataSource.setMaxWait(Integer.valueOf(env.getProperty("jdbc.maxWaitMillis")));
    dataSource.setRemoveAbandonedTimeout(Integer.valueOf(env.getProperty("jdbc.removeAbandonedTimeout")));
    dataSource.setDefaultAutoCommit(Boolean.valueOf(env.getProperty("jdbc.defaultAutoCommit")));

    return dataSource;
  }

  @Bean
  public PlatformTransactionManager transactionManager() {

    final JpaTransactionManager txManager = new JpaTransactionManager();
    txManager.setEntityManagerFactory(entityManagerFactory().getObject());

    return txManager;
  }

  @Bean
  public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
    return new PersistenceExceptionTranslationPostProcessor();
  }

  Properties additionalProperties() {
    return new Properties() {
      {
        setProperty("hibernate.hbm2ddl.auto",
                env.getProperty("hibernate.hbm2ddl.auto"));
        setProperty("hibernate.dialect",
                env.getProperty("hibernate.dialect"));
        setProperty("hibernate.connection.autocommit", env.getProperty("jdbc.defaultAutoCommit"));
        //setProperty("hibernate.show_sql", "true");
        //setProperty("hibernate.globally_quoted_identifiers",
        //        "true");
        //setProperty("hibernate.jdbc.lob.non_contextual_creation", "true");
      }
    };
  }
}
