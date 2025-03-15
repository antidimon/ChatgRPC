package antidimon.web.front.models.dto.chats;

import antidimon.web.front.models.dto.users.ChatUserIdUsernameDTO;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class GroupChatWithIdUsernamesDTO implements ChatToFrontDTO {

    private long chatId;
    private String name;
    private String description;
    private long ownerId;
    private List<ChatUserIdUsernameDTO> users;
    private LocalDateTime createdAt;

}
