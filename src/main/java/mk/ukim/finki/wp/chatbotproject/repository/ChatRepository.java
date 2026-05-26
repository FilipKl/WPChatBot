package mk.ukim.finki.wp.chatbotproject.repository;

import mk.ukim.finki.wp.chatbotproject.models.Chat;
import mk.ukim.finki.wp.chatbotproject.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Chat entity.
 * Provides CRUD operations and custom query methods for Chat.
 */
@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    /**
     * Fetch all chats for a specific user.
     *
     * @param user the User entity
     * @return list of chats owned by the user
     */
    List<Chat> findAllByUser(User user);

    /**
     * Fetch a chat by ID and user, ensuring the chat belongs to the authenticated user.
     *
     * @param id the chat ID
     * @param user the User entity
     * @return Optional containing the Chat if found and belongs to the user
     */
    Optional<Chat> findByIdAndUser(Long id, User user);
}



