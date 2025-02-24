package antidimon.web.userservice.mapper;


import antidimon.web.userservice.models.ChatUser;
import antidimon.web.userservice.models.dto.ChatUserInputDTO;
import antidimon.web.userservice.models.dto.ChatUserOutputDTO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ChatUserMapper  {

    private static final ModelMapper modelMapper = new ModelMapper();

    public ChatUserOutputDTO toDTO(ChatUser chatUser) {
        return modelMapper.map(chatUser, ChatUserOutputDTO.class);
    }

    public ChatUser toEntity(ChatUserInputDTO chatUserInputDTO) {
        return ChatUser.builder()
                .username(chatUserInputDTO.getUsername())
                .name(chatUserInputDTO.getName())
                .age(chatUserInputDTO.getAge())
                .email(chatUserInputDTO.getEmail())
                .phoneNumber(chatUserInputDTO.getPhoneNumber())
                .password(chatUserInputDTO.getPassword())
                .build();
    }
}
