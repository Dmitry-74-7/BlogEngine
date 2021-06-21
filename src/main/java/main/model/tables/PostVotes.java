package main.model.tables;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "post_votes")
@Getter
@Setter
public class PostVotes {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

//  @Column(name = "user_id", nullable = false)
//  private int userId;
  @ManyToOne
  @JoinColumn(name = "user_id")
  private Users user;

//  @Column(name = "post_id", nullable = false)
//  private int postId;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "post_id")
  private Posts post;


  @Column(name = "time", columnDefinition = "DATETIME", nullable = false)
  private Timestamp time;

  @Column(name = "value", columnDefinition = "TINYINT", nullable = false)
  private short value;

}
