package antidimon.web.messageservice.models.dto.chat;

import antidimon.web.messageservice.models.ChatType;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ChatOutputDTO {

    private long chatId;
    private String name;
    private ChatType chatType;
    private long ownerId;
    private long user1Id;
    private long user2Id;
}
