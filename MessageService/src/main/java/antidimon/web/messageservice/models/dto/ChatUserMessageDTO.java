package antidimon.web.messageservice.models.dto;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ChatUserMessageDTO {
    private long id;
    private String username;
    private String phoneNumber;
}
