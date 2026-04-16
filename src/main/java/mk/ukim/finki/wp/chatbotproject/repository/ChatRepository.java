package mk.ukim.finki.wp.chatbotproject.repository;

import mk.ukim.finki.wp.chatbotproject.models.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for Chat entity.
 * Provides CRUD operations and custom query methods for Chat.
 */
@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
}

