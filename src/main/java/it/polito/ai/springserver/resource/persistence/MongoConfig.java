package it.polito.ai.springserver.resource.persistence;

import com.mongodb.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.authentication.UserCredentials;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.ArrayList;
import java.util.List;

@Configuration
@PropertySource("classpath:resource/mongo.properties")
@EntityScan("it.polito.ai.springserver.resource.model.*")
@EnableMongoRepositories("it.polito.ai.springserver.resource.model.repository")
public class MongoConfig extends AbstractMongoConfiguration {

  @Autowired
  private Environment env;

  @Override
  protected String getDatabaseName() {
    return env.getProperty("mongo.dbname");
  }

  @Override
  public Mongo mongo() throws Exception {
    final var credentials = new ArrayList<MongoCredential>();
    /*credentials.add(MongoCredential.createCredential(
        env.getProperty("mongo.username"),
        env.getProperty("mongo.dbname"),
        env.getProperty("mongo.password").toCharArray()));*/
    final var serverAddress = new ServerAddress(
        env.getProperty("mongo.address"),
        Integer.parseInt(env.getProperty("mongo.port")));

    return new MongoClient(serverAddress, credentials);
  }

}
