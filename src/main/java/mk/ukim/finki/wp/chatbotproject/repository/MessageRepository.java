package mk.ukim.finki.wp.chatbotproject.repository;

import mk.ukim.finki.wp.chatbotproject.models.Chat;
import mk.ukim.finki.wp.chatbotproject.models.Message;
import mk.ukim.finki.wp.chatbotproject.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByChatOrderByTimestampAsc(Chat chat);

    @Query("SELECT m FROM Message m WHERE m.chat.id = :chatId AND m.role = :role ORDER BY m.timestamp DESC LIMIT 1")
    Optional<Message> findLastMessageByChat(@Param("chatId") Long chatId, @Param("role") Role role);
}

