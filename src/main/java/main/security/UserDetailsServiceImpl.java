package main.security;

import java.util.List;
import java.util.Optional;
import main.model.repositories.UsersRepository;
import main.model.tables.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//@Service("userDetailsServiceImpl")
@Service
public class UserDetailsServiceImpl implements UserDetailsService {


  private final UsersRepository usersRepository;

  @Autowired
  public UserDetailsServiceImpl(UsersRepository usersRepository) {
    this.usersRepository = usersRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
   // List<Users> users = usersRepository.findEmail(email);

//    if (users.size() == 0) {
//      throw new UsernameNotFoundException("User " + email + " not found");
//    }
//    return SecurityUser.fromUser(users.get(0));

    Users users = usersRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));

    return SecurityUser.fromUser(users);

  }
}
