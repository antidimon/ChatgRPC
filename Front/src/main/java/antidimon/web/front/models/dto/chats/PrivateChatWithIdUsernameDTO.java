package antidimon.web.front.models.dto.chats;

import antidimon.web.front.models.dto.users.ChatUserIdUsernameDTO;
import lombok.*;

import java.time.LocalDateTime;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PrivateChatWithIdUsernameDTO implements ChatToFrontDTO {

    private long chatId;
    private ChatUserIdUsernameDTO user1;
    private ChatUserIdUsernameDTO user2;
    private LocalDateTime createdAt;
}
