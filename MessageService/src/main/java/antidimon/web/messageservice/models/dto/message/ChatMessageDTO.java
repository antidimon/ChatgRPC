package antidimon.web.messageservice.models.dto.message;


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
