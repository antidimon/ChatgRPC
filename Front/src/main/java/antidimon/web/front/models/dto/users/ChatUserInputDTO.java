package antidimon.web.front.models.dto.users;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ChatUserInputDTO {

    private String username;
    private String name;
    private short age;
    private String email;
    private String phoneNumber;
}
