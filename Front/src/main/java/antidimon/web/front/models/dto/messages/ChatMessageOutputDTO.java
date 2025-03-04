package antidimon.web.front.models.dto.messages;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ChatMessageOutputDTO {

    private long id;
    private long senderId;
    private String message;
    private LocalDateTime createdAt;
}
