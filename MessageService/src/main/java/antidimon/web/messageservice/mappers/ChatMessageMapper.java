package antidimon.web.messageservice.mappers;

import antidimon.web.messageservice.models.ChatMessage;
import antidimon.web.messageservice.models.dto.message.ChatMessageOutputDTO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ChatMessageMapper {

    private static final ModelMapper modelMapper = new ModelMapper();

    public ChatMessageOutputDTO toDTO(ChatMessage chatMessage) {
        return modelMapper.map(chatMessage, ChatMessageOutputDTO.class);
    }
}
