package antidimon.web.messageservice.repositories;

import antidimon.web.messageservice.models.ChatParticipant;
import antidimon.web.messageservice.models.ChatParticipantPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, ChatParticipantPK> {

    @Modifying
    @Query(value = "DELETE FROM chat_participants WHERE chat_id = :chat_id AND user_id = :user_id", nativeQuery = true)
    void deleteMember(@Param("chat_id") long id, @Param("user_id") long userId);
}
