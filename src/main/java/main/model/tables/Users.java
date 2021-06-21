package main.model.tables;

import java.sql.Timestamp;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import main.model.auth.Role;

@Entity
@Table(name = "users")
@Getter
@Setter
public class Users {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "is_moderator", columnDefinition = "TINYINT", nullable = false)
  private boolean isModerator;

  @Column(name = "reg_time", columnDefinition = "DATETIME", nullable = false)
  private Timestamp regTime;

  @Column(length = 255, nullable = false)
  private String name;

  @Column(length = 255, nullable = false)
  private String email;

  @Column(length = 255, nullable = false)
  private String password;

  @Column(length = 255)
  private String code;

  @Column(columnDefinition = "TEXT")
  private String photo;

  @OneToMany (mappedBy="user", fetch=FetchType.EAGER)
  private List<Posts> posts;

  @OneToMany (mappedBy="user", fetch=FetchType.LAZY)
  private List<PostVotes> postVotes;

  @OneToMany (mappedBy="user", fetch=FetchType.LAZY)
  private List<PostComments> postComments;

  public Role getRole() {
    return isModerator == true ? Role.MODERATOR : Role.USER;
  }

}
