package antidimon.web.front.models.dto.notifications;

import antidimon.web.front.models.enums.NotificationType;
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
