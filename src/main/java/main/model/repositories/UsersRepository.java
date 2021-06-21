package main.model.repositories;

import java.util.List;
import java.util.Optional;
import main.model.tables.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {

  @Query("SELECT u FROM Users u WHERE u.email = :email")
  List<Users> findEmail(@Param("email") String email);
  Optional<Users> findByEmail(String email);
  Optional<Users> findByCode(String code);
}
