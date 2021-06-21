package main.model.repositories;

import java.util.List;
import java.util.Optional;
import main.model.tables.PostVotes;
import main.model.tables.Tags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostVotesRepository extends JpaRepository<PostVotes, Integer> {
  @Query("SELECT pv FROM PostVotes pv JOIN pv.user u ON u.id = :userId JOIN pv.post p ON p.id = :postId ")
  Optional<PostVotes> findVotes(@Param("userId") int userId, @Param("postId") int postId);

}
