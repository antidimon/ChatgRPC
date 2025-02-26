package antidimon.web.notificationservice.models.dto;


import antidimon.web.notificationservice.models.NotificationType;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class NotificationOutputDTO {

    private long id;
    private NotificationType type;
    private String msg;
    private LocalDateTime createdAt;
}
