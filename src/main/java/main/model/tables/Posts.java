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

@Entity
@Table(name = "posts")
@Getter
@Setter

public class Posts {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "is_active", columnDefinition = "TINYINT", nullable = false)
  private boolean isActive;

  @Enumerated(EnumType.STRING)
  @Column(name = "moderation_status", columnDefinition = "enum('NEW', 'ACCEPTED', 'DECLINED')", nullable = false)
  private ModerationStatus moderationStatus;

  @Column(name = "moderator_id")
  private int moderatorId;

  //@Column(name = "user_id", nullable = false)
  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn (name="user_id")
  private Users user;
  //private int userId;

  @Column(name = "time", columnDefinition = "DATETIME", nullable = false)
  private Timestamp time;

  @Column(length = 255, nullable = false)
  private String title;

  @Column(columnDefinition = "TEXT", nullable = false)
  private String text;

  @Column(name = "view_count", nullable = false)
  private int viewCount;

  @OneToMany (mappedBy="post", fetch=FetchType.LAZY)
  private List<PostVotes> postVotes;

  @OneToMany (mappedBy="post", fetch=FetchType.LAZY)
  private List<PostComments> postComments;


//  @ManyToMany(fetch = FetchType.LAZY)
//  @JoinTable(name = "tag2post",
//      joinColumns = {@JoinColumn(name = "post_id")},
//      inverseJoinColumns = {@JoinColumn(name = "tag_id")})
//  private List<Tags> tags;
  @ManyToMany(mappedBy = "posts")
  private List<Tags> tags;



}
