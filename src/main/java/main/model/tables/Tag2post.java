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
@Table(name = "tag2post")
@Getter
@Setter
public class Tag2post {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "post_id", nullable = false)
  private int postId;

  @Column(name = "tag_id", nullable = false)
  private int tagId;
}
