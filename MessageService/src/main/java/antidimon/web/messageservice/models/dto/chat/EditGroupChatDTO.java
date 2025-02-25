package antidimon.web.messageservice.models.dto.chat;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class EditGroupChatDTO {

    private long chatId;
    private String name;
    private String description;
}
