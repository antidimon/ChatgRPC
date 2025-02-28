package antidimon.web.messageservice.repositories;

import antidimon.web.messageservice.models.Chat;
import antidimon.web.messageservice.models.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    @Modifying
    @Query(value = "DELETE FROM messages WHERE sender_id = :id", nativeQuery = true)
    void deleteAllBySenderId(@Param("id") long userId);
}
