package antidimon.web.notificationservice.services.inner;


import antidimon.web.notificationservice.mappers.NotificationMapper;
import antidimon.web.notificationservice.models.Notification;
import antidimon.web.notificationservice.models.NotificationType;
import antidimon.web.notificationservice.models.dto.NotificationOutputDTO;
import antidimon.web.notificationservice.repositories.NotificationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class NotificationService {

    private NotificationRepository notificationRepository;
    private NotificationMapper notificationMapper;

    @Transactional
    protected void save(Notification notification) {
        notificationRepository.save(notification);
    }

    @Transactional
    public void sendNotification(long userId, String type, String message) {
        Notification notification = Notification.builder()
                .userId(userId)
                .type(NotificationType.valueOf(type))
                .msg(message)
                .build();

        this.save(notification);
    }

    public List<NotificationOutputDTO> getNotifications(long userId) {
        List<Notification> notifications = this.notificationRepository.getNotificationsByUserId(userId);

        return notifications.stream().map(notificationMapper::toDTO).toList();
    }
}
