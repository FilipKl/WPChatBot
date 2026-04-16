package mk.ukim.finki.wp.chatbotproject.repository;

import mk.ukim.finki.wp.chatbotproject.models.Chat;
import mk.ukim.finki.wp.chatbotproject.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Message entity.
 * Provides CRUD operations and custom query methods for Message.
 */
@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    /**
     * Fetch all messages for a given chat ordered by timestamp in ascending order.
     *
     * @param chat the Chat entity to fetch messages for
     * @return list of messages ordered by timestamp ascending
     */
    List<Message> findByChatOrderByTimestampAsc(Chat chat);
}

