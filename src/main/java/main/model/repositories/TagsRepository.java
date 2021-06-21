package main.model.repositories;

import java.util.List;
import main.model.tables.Tags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TagsRepository extends JpaRepository<Tags, Integer> {
    @Query("SELECT MAX(posts.size) from Tags t")
    Integer maxPostCount();

    @Query("SELECT t from Tags t where t.name =:query")
    List<Tags> tagsListQuery(@Param("query") String query);

}
