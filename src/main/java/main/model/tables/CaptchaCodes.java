package main.model.tables;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "captcha_codes")
@Getter
@Setter
public class CaptchaCodes {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "time", columnDefinition = "DATETIME", nullable = false)
  private Timestamp time;

  @Column(name = "code", columnDefinition = "TINYTEXT", length = 255, nullable = false)
  private String  code;

  @Column(name = "secret_code", columnDefinition = "TINYTEXT", length = 255, nullable = false)
  private String  secretCode;
}
