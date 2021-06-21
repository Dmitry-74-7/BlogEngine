package main.model.repositories;

import java.sql.Timestamp;
import java.util.List;
import main.model.tables.CaptchaCodes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CaptchaRepository extends JpaRepository<CaptchaCodes, Integer> {

//  @Query("DELETE CaptchaCodes c "
//      + "WHERE DATEDIFF(CURDATE(), HOUR(c.time)) <> 0 OR "
//      + "ABS(HOUR(c.time) - HOUR(CURTIME())) >= :hour")
//  void deleteCaptcha(@Param("hour") int hour);



  @Query("SELECT c FROM CaptchaCodes c "
      + "WHERE DATEDIFF(CURDATE(), c.time) <> 0 OR "
      + "ABS(HOUR(c.time) - HOUR(CURTIME())) >= :hour")
  List<CaptchaCodes> oldDate(@Param("hour") int hour);

  @Query("SELECT c FROM CaptchaCodes c "
      + "WHERE c.secretCode = :secretCode")
  List<CaptchaCodes> getCodeCaptcha(@Param("secretCode") String secretCode);


}
