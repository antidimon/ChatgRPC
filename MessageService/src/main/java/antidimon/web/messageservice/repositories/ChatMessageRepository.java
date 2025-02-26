package antidimon.web.messageservice.repositories;

import antidimon.web.messageservice.models.Chat;
import antidimon.web.messageservice.models.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    void deleteChatMessagesByChat(Chat chat);
}
