package it.polito.ai.authorization_server.spring;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
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

@Configuration
@EnableTransactionManagement
@PropertySource("classpath:postgres.properties")
@ComponentScan("it.polito.ai.authorization_server")
@EnableJpaRepositories(basePackages = "it.polito.ai.authorization_server.model")
public class PersistenceConfig {

  @Autowired
  private Environment env;

  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory(){
    final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
    em.setDataSource(restDataSource());
    em.setPackagesToScan("it.polito.ai.authorization_server.model");
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
    dataSource.setUrl(env.getProperty("jdbc.url"));
    dataSource.setUsername(env.getProperty("jdbc.username"));
    dataSource.setPassword(env.getProperty("jdbc.password"));
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
        //setProperty("hibernate.globally_quoted_identifiers",
        //        "true");
        setProperty("hibernate.jdbc.lob.non_contextual_creation", "true");
      }
    };
  }
}
