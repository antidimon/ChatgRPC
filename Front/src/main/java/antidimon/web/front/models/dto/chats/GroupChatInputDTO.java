package antidimon.web.front.models.dto.chats;


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
