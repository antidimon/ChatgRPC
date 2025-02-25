package antidimon.web.messageservice.models.dto.chat;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class GroupChatInputDTO {

    private long ownerId;
    private String name;
    private String description;
}
