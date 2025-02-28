package antidimon.web.notificationservice.services.inner;


import antidimon.web.notificationservice.mappers.NotificationMapper;
import antidimon.web.notificationservice.models.Notification;
import antidimon.web.notificationservice.models.NotificationType;
import antidimon.web.notificationservice.models.dto.NotificationOutputDTO;
import antidimon.web.notificationservice.repositories.NotificationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class NotificationService {

    private NotificationRepository notificationRepository;
    private NotificationMapper notificationMapper;
    private SubscriptionService subscriptionService;

    @Transactional
    protected void save(Notification notification) {
        notificationRepository.save(notification);
    }

    @Transactional
    public boolean sendNotification(long userId, String type, String message) {

        boolean flag = this.subscriptionService.isUserSubscribed(userId, type);
        if (flag) {
            Notification notification = Notification.builder()
                    .userId(userId)
                    .type(NotificationType.valueOf(type))
                    .msg(message)
                    .build();

            this.save(notification);
            return true;
        }else {
            return false;
        }
    }

    public List<NotificationOutputDTO> getNotifications(long userId) {
        List<Notification> notifications = this.notificationRepository.getNotificationsByUserId(userId);

        return notifications.stream().map(notificationMapper::toDTO).toList();
    }

    @Transactional
    public void deleteAllNotifications(long userId) {
        this.notificationRepository.deleteAllByUserId(userId);
    }
}
