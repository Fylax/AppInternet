package it.polito.ai.springserver;

import it.polito.ai.springserver.authorization.model.OAuth2Client;
import it.polito.ai.springserver.authorization.model.Role;
import it.polito.ai.springserver.authorization.model.User;
import it.polito.ai.springserver.authorization.model.UserStatus;
import it.polito.ai.springserver.authorization.model.repository.OAuth2ClientRepositoryInterface;
import it.polito.ai.springserver.authorization.model.repository.UserRepositoryInterface;
import it.polito.ai.springserver.resource.model.repository.PositionRepositoryInterface;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class SpringserverApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringserverApplication.class, args);
  }


  @Bean
  CommandLineRunner init(UserRepositoryInterface userRepository,
                         OAuth2ClientRepositoryInterface clientRepository,
                         PositionRepositoryInterface positionRepository) {
    return new CommandLineRunner() {
      @Override
      public void run(String... args) throws Exception {
        clientRepository.save(new OAuth2Client("client", "secret"));

        var u1 = userRepository.save(
                new User(
                        "pippo",
                        "123456",
                        "pippo@gmail.com",
                        UserStatus.APPROVED));
        var u2 = userRepository.save(
                new User(
                        "pluto",
                        "123456",
                        "pluto@gmail.com",
                        UserStatus.APPROVED));
        userRepository.save(
                new User(
                        "paperino",
                        "123456",
                        "paperino@gmail.com",
                        UserStatus.APPROVED));

        u1.addRole(Role.ROLE_ADMIN);
        userRepository.save(u1);
        userRepository.save(u2);
      }
    };
  }
}
