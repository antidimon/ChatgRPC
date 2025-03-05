package antidimon.web.front.models.dto.messages;


import antidimon.web.front.models.enums.MessageType;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class FrontMessageDTO {

    private long id;
    private long senderId;
    private String senderUsername;
    private String message;
    private MessageType messageType;
    private LocalDateTime createdAt;
}
