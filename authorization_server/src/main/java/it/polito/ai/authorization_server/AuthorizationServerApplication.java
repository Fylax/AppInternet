package it.polito.ai.authorization_server;
import it.polito.ai.authorization_server.model.User;
import it.polito.ai.authorization_server.model.repository.UserRepository;
import it.polito.ai.authorization_server.model.UserStatus;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AuthorizationServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(AuthorizationServerApplication.class, args);
  }

  @Bean
  CommandLineRunner init (UserRepository userRepository){
    return new CommandLineRunner() {
      @Override
      public void run(String... args) throws Exception {
        userRepository.save(
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
      }
    };
  }
}
