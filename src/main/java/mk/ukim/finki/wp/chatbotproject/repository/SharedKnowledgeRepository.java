package mk.ukim.finki.wp.chatbotproject.repository;

import mk.ukim.finki.wp.chatbotproject.models.SharedKnowledge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SharedKnowledgeRepository extends JpaRepository<SharedKnowledge, Long> {
    List<SharedKnowledge> findAllByCreatedAtAfter(LocalDateTime after);

    @Query("SELECT sk FROM SharedKnowledge sk WHERE LOWER(sk.question) LIKE LOWER(CONCAT('%', :query, '%')) ORDER BY sk.createdAt DESC LIMIT 3")
    List<SharedKnowledge> searchByQuestion(@Param("query") String query);

    @Query("SELECT sk FROM SharedKnowledge sk WHERE LOWER(sk.question) = LOWER(:question)")
    Optional<SharedKnowledge> findByQuestionExact(@Param("question") String question);
}
