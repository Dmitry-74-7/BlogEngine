package main.model.repositories;

import java.util.List;
import java.util.Optional;
import main.model.tables.PostComments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostCommentsRepository extends JpaRepository<PostComments, Integer> {
  List<PostComments> findByPostId(Integer postId);
  Optional<PostComments> findById(Integer id);
}
