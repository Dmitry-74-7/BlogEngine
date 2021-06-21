package main.other;

import java.util.Optional;
import main.model.repositories.UsersRepository;
import main.model.tables.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationUser {

  private final UsersRepository usersRepository;

  @Autowired
  public AuthenticationUser(UsersRepository usersRepository) {
    this.usersRepository = usersRepository;
  }
  public  Users getUser() {

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth.getPrincipal().toString().equals("anonymousUser")) {
      return null;
    }

    User user = (User) auth.getPrincipal();

    Optional<Users> currentUser = usersRepository.findByEmail(user.getUsername());
    if (currentUser.isPresent()) {
        return currentUser.get();
    }
    return null;
  }
}
