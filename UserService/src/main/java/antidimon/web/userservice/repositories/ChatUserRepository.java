package antidimon.web.userservice.repositories;

import antidimon.web.userservice.models.ChatUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ChatUserRepository extends JpaRepository<ChatUser, Long> {
    Optional<ChatUser> findByUsername(String username);

    Optional<ChatUser> findByEmail(String email);

    Optional<ChatUser> findByPhoneNumber(String phoneNumber);


    @Query(value = "SELECT * FROM users u WHERE u.username LIKE CONCAT(:regex, '%')", nativeQuery = true)
    List<ChatUser> findAllByUsernameRegex(@Param("regex") String regex);
}
