package it.polito.ai.springserver.authorization.controller;

import it.polito.ai.springserver.authorization.model.User;
import it.polito.ai.springserver.authorization.model.UserStatus;
import it.polito.ai.springserver.authorization.model.repository.UserRepositoryInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/oauth/register")
public class RegistrationController {

  @Autowired
  private UserRepositoryInterface userRepository;

  private final Pattern emailRegex = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");

  private final Pattern pwdLowerRegex = Pattern.compile("(\\p{Lower})");
  private final Pattern pwdUpperRegex = Pattern.compile("(\\p{Upper})");
  private final Pattern pwdDigitRegex = Pattern.compile("(\\p{Digit})");
  private final Pattern pwdSymbolRegex = Pattern.compile("[\\p{Print}&&\\P{Alnum}]");

  @GetMapping(produces = "application/json")
  public boolean usable(@RequestParam("type") String type, @RequestParam("value") String value) {
    if (type.equalsIgnoreCase("username")) {
      return !userRepository.existsByUsername(value);
    } else if (type.equalsIgnoreCase("email")) {
      return !userRepository.existsByEmail(value);
    }
    return true;
  }

  @PostMapping(consumes = "application/x-www-form-urlencoded", produces = "text/plain")
  public ResponseEntity<String> register(@RequestParam Map<String, String> data) {
    var username = data.get("username");
    var password = data.get("password");
    var email = data.get("email");
    if (username == null || password == null || email == null ||
        username.isEmpty() || password.isEmpty() || email.isEmpty()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing fields.");
    }

    if (userRepository.existsByUsername(username)) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists.");
    }

    if (userRepository.existsByEmail(email)) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already used.");
    }

    if (!emailRegex.matcher(email).matches()) {
      return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
          .body("Invalid email.");
    }

    if (password.length() < 8) {
      return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
          .body("Password must be at least of 8 characters.");
    }
    if (password.contains(username)) {
      return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
          .body("Password cannot contain username.");
    }
    boolean low = pwdLowerRegex.matcher(password).find();
    boolean up = pwdUpperRegex.matcher(password).find();
    boolean dig = pwdDigitRegex.matcher(password).find();
    boolean sym = pwdSymbolRegex.matcher(password).find();
    if ((Boolean.compare(low, false) + Boolean.compare(up, false) +
         Boolean.compare(dig, false) + Boolean.compare(sym, false)) < 3) {
      return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
          .body("Insecure password, use more symbols.");
    }


    var user = new User(username, password, email, UserStatus.APPROVED);
    userRepository.save(user);

    return ResponseEntity.created(URI.create("/")).body(null);
  }
}
