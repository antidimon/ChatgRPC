package antidimon.web.messageservice.models.dto.chat;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class PrivateChatOutputDTO {

    private long user1Id;
    private long user2Id;
    private LocalDateTime createdAt;
}
