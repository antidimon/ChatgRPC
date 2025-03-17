package antidimon.web.front.repositories;

import antidimon.web.front.security.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MyUserRepository extends JpaRepository<MyUser, Long> {
    Optional<MyUser> findByUsername(String username);


    @Query(value = "DELETE FROM users WHERE username = :username", nativeQuery = true)
    @Modifying
    void deleteByUsername(@Param("username") String username);
}
