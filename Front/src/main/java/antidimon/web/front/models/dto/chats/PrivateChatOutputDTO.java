package antidimon.web.front.models.dto.chats;


import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class PrivateChatOutputDTO implements ChatgRPCDTO {

    private long chatId;
    private long user1Id;
    private long user2Id;
    private LocalDateTime createdAt;
}
