package main.model.tables;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "global_settings")
@Getter
@Setter
public class GlobalSettings {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "code", length = 255, nullable = false)
  private String  code;

  @Column(name = "name", length = 255, nullable = false)
  private String  name;

  @Column(name = "value", length = 255, nullable = false)
  private String  value;
}
