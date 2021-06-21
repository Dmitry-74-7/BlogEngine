package main.model.tables;

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
@Table(name = "tags")
@Getter
@Setter
public class Tags {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(length = 255, nullable = false)
  private String name;

  @ManyToMany(cascade = CascadeType.ALL)
  @JoinTable(name = "tag2post",
      joinColumns = {@JoinColumn(name = "tag_id")},
      inverseJoinColumns = {@JoinColumn(name = "post_id")})
  private List<Posts> posts;
}
