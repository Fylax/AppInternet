package it.polito.ai.springserver;

import it.polito.ai.springserver.model.Role;
import it.polito.ai.springserver.model.User;
import it.polito.ai.springserver.model.UserStatus;
import it.polito.ai.springserver.model.repository.UserRepository;
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
  CommandLineRunner init (UserRepository userRepository){
    return new CommandLineRunner() {
      @Override
      public void run(String... args) throws Exception {
        var u1 = userRepository.save(
                new User(
                        "pippo",
                        "$2a$08$OL4g6FUS8taKbWrLXjErrOpk6VJX0mM2S2DIaYmpDIqQDBlM4niXe",
                        "pippo@gmail.com",
                        UserStatus.APPROVED));
        userRepository.save(
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

        u1.addRole(Role.ADMIN);
        userRepository.save(u1);
      }
    };
  }
}
