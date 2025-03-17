package antidimon.web.userservice.models.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ChatUserInputDTO {

    private String username;
    private String name;
    private Short age;
    private String email;
    private String phoneNumber;
}
