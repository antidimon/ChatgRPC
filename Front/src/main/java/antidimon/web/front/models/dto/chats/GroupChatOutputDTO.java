package antidimon.web.front.models.dto.chats;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class GroupChatOutputDTO implements ChatgRPCDTO{

    private long chatId;
    private String name;
    private String description;
    private long ownerId;
    private List<Long> membersIds;
    private LocalDateTime createdAt;
}
