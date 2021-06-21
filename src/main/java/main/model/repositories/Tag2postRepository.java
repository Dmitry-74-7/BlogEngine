package main.model.repositories;

import java.util.List;
import java.util.Optional;
import main.model.tables.Posts;
import main.model.tables.Tag2post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface Tag2postRepository extends JpaRepository<Tag2post, Integer> {
  @Query("SELECT tp FROM Tag2post tp WHERE tp.postId = :postId AND tp.tagId = :tagId ")
  Optional<Tag2post> findPostTag(@Param("postId") int postId, @Param("tagId") int tagId);
}
