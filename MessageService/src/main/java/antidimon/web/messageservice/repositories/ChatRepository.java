package antidimon.web.messageservice.repositories;

import antidimon.web.messageservice.models.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    @Query(value = "SELECT * FROM chats WHERE user1_id = :id OR user2_id = :id", nativeQuery = true)
    List<Chat> findUserPrivateChats(@Param("id") long userId);

    @Query(value = "SELECT c.* FROM chat_participants cs LEFT JOIN chats c ON c.id = cs.chat_id WHERE cs.user_id = :id", nativeQuery = true)
    List<Chat> findUserMemberChats(@Param("id") long userId);

    @Query(value = "SELECT * FROM chats WHERE (user1_id = :id1 AND user2_id = :id2) OR (user1_id = :id2 AND user2_id = :id1)", nativeQuery = true)
    Optional<Chat> findByUsers(@Param("id1") long creatorId, @Param("id2") long user2Id);
}
