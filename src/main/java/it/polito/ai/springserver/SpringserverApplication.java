package it.polito.ai.springserver;

import it.polito.ai.springserver.authorization.model.Role;
import it.polito.ai.springserver.authorization.model.User;
import it.polito.ai.springserver.authorization.model.UserStatus;
import it.polito.ai.springserver.authorization.model.repository.UserRepositoryInterface;
import it.polito.ai.springserver.resource.model.Position;
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
  CommandLineRunner init (UserRepositoryInterface userRepository, PositionRepositoryInterface positionRepositoryInterface){
    return new CommandLineRunner() {
      @Override
      public void run(String... args) throws Exception {
        var u1 = userRepository.save(
                new User(
                        "pippo",
                        "$2a$08$OL4g6FUS8taKbWrLXjErrOpk6VJX0mM2S2DIaYmpDIqQDBlM4niXe",
                        "pippo@gmail.com",
                        UserStatus.APPROVED));
        var u2 = userRepository.save(
                new User(
                        "pluto",
                        "$2a$08$NWWDc6E61T9S5Ohs0NaJ/ujHpnL448Jiokd5LF/v4B.d6xCryOQ5m",
                        "pluto@gmail.com",
                        UserStatus.APPROVED));
        userRepository.save(
                new User(
                        "paperino",
                        "$2a$08$SdwzOJhH85lidEkozGwTJ.V7Pr.vGu413raF6E1Vzzv45GfMIxMIK",
                        "paperino@gmail.com",
                        UserStatus.APPROVED));

        u1.addRole(Role.ROLE_ADMIN);
        u2.addRole(Role.ROLE_CUSTOMER);
          userRepository.save(u1);
          userRepository.save(u2);

        //positionRepositoryInterface.save(new Position(1, 10, 15.3, 7.14));
        /*positionRepository.save(new Position(1, 11, 7.68, 45.07));
        positionRepository.save(new Position(1, 12, 7.68, 45.07));
        positionRepository.save(new Position(1, 13, 7.68, 45.07));
        positionRepository.save(new Position(1, 14, 7.68, 45.07));
        positionRepository.save(new Position(1, 15, 7.68, 45.07));
*/

      }
    };
  }

 /* @Bean
  CommandLineRunner init(PositionRepository positionRepository) {
    return new CommandLineRunner() {
      @Override
      public void run(String... args) throws Exception {
      }
    };
  }*/
}
