package antidimon.web.messageservice.repositories;

import antidimon.web.messageservice.models.ChatParticipant;
import antidimon.web.messageservice.models.ChatParticipantPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, ChatParticipantPK> {
}
