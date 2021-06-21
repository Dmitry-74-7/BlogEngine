package main.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

//  private final UserDetailsService userDetailsService;
//
//  //@Qualifier("userDetailsServiceImpl")
//  @Autowired
//  public SecurityConfig(@Qualifier("userDetailsServiceImpl")
//      UserDetailsService userDetailsService) {
//    this.userDetailsService = userDetailsService;
//  }

  private final UserDetailsService userDetailsService;

  @Autowired
  public SecurityConfig(UserDetailsService userDetailsService) {
    this.userDetailsService = userDetailsService;
  }


  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable()
        .authorizeRequests()
        .antMatchers("/**").permitAll()
//        .antMatchers(HttpMethod.GET,"/api/post").hasAnyAuthority(Permission.USER.getPermission())
//        .antMatchers(HttpMethod.GET,"/api/post/search*").hasAnyAuthority(Permission.MODERATE.getPermission())
        .anyRequest()
        .authenticated()
        .and()
//        .formLogin().permitAll()
        .formLogin().disable()
        .logout()
        .and()
        .httpBasic();
  }

//  @Bean
//  protected UserDetailsService userDetailsService() {
//   // PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
//    return new InMemoryUserDetailsManager(
//        User.builder()
//            .username("user")
//            .password(passwordEncoder().encode("user"))
//            .authorities(Role.USER.getAuthorities())
//            .build(),
//        User.builder()
//          .username("moderator")
//          .password(passwordEncoder().encode("moderator"))
//          .authorities(Role.MODERATOR.getAuthorities())
//          .build());
//  }

  @Bean
  protected DaoAuthenticationProvider daoAuthenticationProvider() {
    DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
    daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

    daoAuthenticationProvider.setUserDetailsService(userDetailsService);
    return daoAuthenticationProvider;
  }

  @Bean
  public static PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(12);
  }

  @Override
  @Bean
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }




}
