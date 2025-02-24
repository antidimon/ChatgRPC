package antidimon.web.userservice.models.dto;


import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ChatUserOutputDTO {

    private String username;
    private String name;
    private short age;
    private String email;
    private String phoneNumber;
    private LocalDateTime createdAt;

}
