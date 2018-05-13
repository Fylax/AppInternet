package it.polito.ai.springserver.resource.persistence;

import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;

@Configuration
@PropertySource("classpath:mongo.properties")
public class MongoConfig extends AbstractMongoConfiguration {

  @Autowired
  private Environment env;

  @Override
  protected String getDatabaseName() {
    return env.getProperty("mongo.dbname");
  }


  @Override
  public MongoClient mongoClient() {
    return new MongoClient(env.getProperty("mongo.address"),
                           Integer.parseInt(env.getProperty("mongo.port")));
  }


}
