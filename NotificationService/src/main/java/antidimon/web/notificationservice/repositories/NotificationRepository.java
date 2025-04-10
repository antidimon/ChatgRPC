package antidimon.web.notificationservice.repositories;

import antidimon.web.notificationservice.models.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> getNotificationsByUserId(long userId);

    @Modifying
    void deleteAllByUserId(long userId);
}
