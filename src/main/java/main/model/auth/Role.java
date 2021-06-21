package main.model.auth;

import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Getter
@AllArgsConstructor
public enum Role {
  USER(Set.of(Permission.USER)),
  MODERATOR(Set.of(Permission.USER, Permission.MODERATE));

  private final Set<Permission> permissions;

  public Set<SimpleGrantedAuthority> getAuthorities () {
    return permissions.stream().map(e -> new SimpleGrantedAuthority(e.getPermission())).collect(
        Collectors.toSet());
  }
}
