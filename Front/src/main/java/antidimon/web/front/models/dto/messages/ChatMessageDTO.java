package antidimon.web.front.models.dto.messages;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ChatMessageDTO {

    private long chatId;
    private String message;
    private long senderId;
}
