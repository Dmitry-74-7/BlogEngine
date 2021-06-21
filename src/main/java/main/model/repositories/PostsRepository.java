package main.model.repositories;


import java.util.List;
import java.util.Map;
import java.util.Optional;
import main.model.tables.Posts;
import main.service.classes.CalendarStatistic;
import main.service.classes.Comments;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostsRepository extends JpaRepository<Posts, Integer> {
    // api/posts
    @Query("SELECT p FROM Posts p "
        + "WHERE p.isActive = true AND p.moderationStatus = 'ACCEPTED' AND "
        + "p.time <= SYSDATE()")
    List<Posts> findPosts(PageRequest pageRequest);

    @Query("SELECT p FROM Posts p "
        + "WHERE p.isActive = true AND p.moderationStatus = 'ACCEPTED' AND "
        + "p.time <= SYSDATE()")
    List<Posts> findPosts(Sort sort);

    @Query("SELECT Count(p) FROM Posts p "
        + "WHERE p.isActive = true AND p.moderationStatus = 'ACCEPTED' AND "
        + "p.time <= SYSDATE()")
    int countPosts();

    @Query("SELECT p FROM Posts p "
            + "JOIN p.postVotes pv ON pv.value=1 "
        + "WHERE p.isActive = true AND p.moderationStatus = 'ACCEPTED' AND "
        + "p.time <= SYSDATE()"
        + "GROUP BY p.title ORDER BY SUM(pv.value) desc")
    List<Posts> likePosts();

    //api/search
    @Query("SELECT p FROM Posts p " +
            "WHERE p.isActive = true AND " +
            "p.moderationStatus = 'ACCEPTED' AND p.time <= SYSDATE() AND " +
            "p.title LIKE CONCAT('%',:query,'%')")
    List<Posts> searchPosts(Pageable pageable, @Param("query") String query);

    //api/Calendar
    @Query("SELECT p FROM Posts p " +
        "WHERE p.isActive = true AND " +
        "p.moderationStatus = 'ACCEPTED' AND p.time <= SYSDATE() AND " +
        "YEAR(time) = :year")
    List<Posts> postsYear(Pageable pageable, @Param("year") String year);


    @Query("SELECT YEAR(time) AS y FROM Posts p "
        + "WHERE p.isActive = true AND p.moderationStatus = 'ACCEPTED' AND p.time <= SYSDATE()"
        + "GROUP BY y ORDER BY y ")
    List<Integer> yearList();


    @Query("SELECT DATE_FORMAT(time, '%Y-%m-%d') as date, CAST(SUM(1) AS string) as count FROM Posts p " +
        "WHERE p.isActive = true AND " +
        "p.moderationStatus = 'ACCEPTED' AND p.time <= SYSDATE() AND " +
        "YEAR(time) = :year "
        + "GROUP BY date ORDER BY date")
    List<Map<String, String>> dateList(@Param("year") int year);


    //api/byDate
    @Query("SELECT p FROM Posts p " +
        "WHERE p.isActive = true AND " +
        "p.moderationStatus = 'ACCEPTED' AND p.time <= SYSDATE() AND " +
        "DATE_FORMAT(time, '%Y-%m-%d') = :date")
    List<Posts> postsDate(Pageable pageable, @Param("date") String date);

    // api/byTag
    @Query("SELECT p FROM Posts p "
        + "JOIN p.tags t ON t.name=:tag "
        + "WHERE p.isActive = true AND p.moderationStatus = 'ACCEPTED' AND "
        + "p.time <= SYSDATE()")
    List<Posts> findPostsByTag(Pageable pageable, @Param("tag") String tag);

    //api/Post{id}
    @Query("SELECT p FROM Posts p WHERE p.time <= SYSDATE() and p.id = :id")
      //  + "WHERE p.isActive = true AND "
     //   + "p.time <= SYSDATE() and p.id = :id")
    List<Posts> findPostsId(@Param("id") int id);

    @Query("SELECT p FROM Posts p WHERE p.title = :title AND p.text = :text")
    Optional<Posts> findPostByTitleAndText(@Param("title") String title, @Param("text") String text);

    @Query("SELECT ABS(SUM(pv.value)) FROM Posts p "
        + "JOIN p.postVotes pv ON pv.value= :likeDislike "
        + "WHERE p.isActive = true AND p.moderationStatus = 'ACCEPTED' AND "
        + "p.time <= SYSDATE() and p.id = :id")
    Integer getLikeDislikeCountById(@Param("id") int id, @Param("likeDislike") short likeDislike);


    @Query("SELECT pt.name FROM Posts p "
        + "JOIN p.tags pt "
        + "WHERE p.isActive = true AND p.moderationStatus = 'ACCEPTED' AND "
        + "p.time <= SYSDATE() AND p.id = :id")
    List<String> getTagList(@Param("id") int id);

    @Query("SELECT NEW main.service.classes.Comments(pc.id, Cast(UNIX_TIMESTAMP(pc.time) as long), pc.text, pvu.id, pvu.name, pvu.photo) FROM Posts p "
        + "JOIN p.postComments pc on pc <>''"
        + "JOIN pc.user pvu on pvu <>''"
        + "WHERE p.isActive = true AND p.moderationStatus = 'ACCEPTED' AND "
        + "p.time <= SYSDATE() AND p.id = :id")
    List<Comments> getUserList(@Param("id") int id);

    //api/post/moderation
    @Query("SELECT p FROM Posts p "
        + "WHERE p.isActive = true AND p.moderationStatus = UCASE(:status)")
    List<Posts> moderationActionPosts(Pageable pageable, @Param("status") String status);

    @Query("SELECT p FROM Posts p "
        + "WHERE p.isActive = true AND p.moderationStatus = UCASE(:status)")
    List<Posts> moderationActionPosts( @Param("status") String status);

    @Query("SELECT p FROM Posts p "
        + "WHERE p.isActive = true AND p.moderationStatus = UCASE(:status) AND p.moderatorId = :id")
    List<Posts> moderationActionPosts(Pageable pageable, @Param("status") String status,
        @Param("id") int id);

    // api/post/my
    @Query("SELECT p FROM Posts p "
        + "JOIN p.user u ON u.id = :id "
        + "WHERE p.isActive = false")
    List<Posts> findMyNoActionPosts(Pageable pageable,
        @Param("id") int id);

    @Query("SELECT p FROM Posts p "
        + "JOIN p.user u ON u.id = :id "
        + "WHERE p.isActive = true AND p.moderationStatus = UCASE(:status)")
    List<Posts> findMyActionPosts(Pageable pageable,  @Param("status") String status,
        @Param("id") int id);

    // api/statistics/my
    @Query("SELECT COUNT(p) FROM Posts p "
        + "JOIN p.user u ON u.id = :id "
        + "WHERE p.isActive = true AND p.moderationStatus = 'ACCEPTED' AND "
        + "DATE(p.time)<= CURRENT_DATE()")
    Integer myPostCount (@Param("id") int userId);
//
    @Query("SELECT ABS(SUM(pv.value)) FROM Posts p "
        + "JOIN p.user u ON u.id = :id "
        + "JOIN p.postVotes pv ON pv.value = :ld "
        + "WHERE p.isActive = true AND p.moderationStatus = 'ACCEPTED' AND "
        + "p.time <= SYSDATE()")
    Integer myLikeDislikeCount (@Param("id") int userId, @Param("ld") short likeDislike);
//
    @Query("SELECT SUM(p.viewCount) FROM Posts p "
        + "JOIN p.user u ON u.id = :id "
        + "WHERE p.isActive = true AND p.moderationStatus = 'ACCEPTED' AND "
        + "p.time <= SYSDATE()")
    Integer myViewCount (@Param("id") int userId);

    // api/statistics/all
    @Query("SELECT COUNT(p) FROM Posts p "
        + "WHERE p.isActive = true AND p.moderationStatus = 'ACCEPTED' AND "
        + "p.time <= SYSDATE()")
    Integer PostCount ();
    //
    @Query("SELECT ABS(SUM(pv.value)) FROM Posts p "
        + "JOIN p.postVotes pv ON pv.value = :ld "
        + "WHERE p.isActive = true AND p.moderationStatus = 'ACCEPTED' AND "
        + "p.time <= SYSDATE()")
    Integer LikeDislikeCount (@Param("ld") short likeDislike);
    //
    @Query("SELECT ABS(SUM(p.viewCount)) FROM Posts p "
        + "WHERE p.isActive = true AND p.moderationStatus = 'ACCEPTED' AND "
        + "p.time <= SYSDATE()")
    Integer ViewCount ();
}
