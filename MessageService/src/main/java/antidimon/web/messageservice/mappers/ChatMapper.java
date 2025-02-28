package antidimon.web.messageservice.mappers;

import antidimon.web.messageservice.models.Chat;
import antidimon.web.messageservice.models.ChatMessage;
import antidimon.web.messageservice.models.ChatType;
import antidimon.web.messageservice.models.dto.chat.ChatOutputDTO;
import antidimon.web.messageservice.models.dto.chat.GroupChatInputDTO;
import antidimon.web.messageservice.models.dto.chat.GroupChatOutputDTO;
import antidimon.web.messageservice.models.dto.chat.PrivateChatOutputDTO;
import antidimon.web.messageservice.models.dto.message.ChatMessageDTO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class ChatMapper {

    private static final ModelMapper modelMapper = new ModelMapper();

    public ChatMessage toEntity(ChatMessageDTO chatMessageDTO, Chat chat) {
        return ChatMessage.builder()
                .chat(chat)
                .senderId(chatMessageDTO.getSenderId()).message(chatMessageDTO.getMessage())
                .build();
    }

    public Chat toEntity(GroupChatInputDTO chatInputDTO) {
        return Chat.builder()
                .name(chatInputDTO.getName())
                .description(chatInputDTO.getDescription())
                .ownerId(chatInputDTO.getOwnerId())
                .type(ChatType.GROUP)
                .members(new ArrayList<>())
                .build();
    }

    public GroupChatOutputDTO toGroupOutputDTO(Chat chat) {
        var list = chat.getMembers().stream().map(member -> member.getId().getUserId()).toList();
        return GroupChatOutputDTO.builder()
                .chatId(chat.getId())
                .name(chat.getName())
                .description(chat.getDescription())
                .ownerId(chat.getOwnerId())
                .membersIds(list)
                .createdAt(chat.getCreatedAt())
                .build();
    }

    public ChatOutputDTO toOutputDTO(Chat chat){
        return modelMapper.map(chat, ChatOutputDTO.class);
    }

    public PrivateChatOutputDTO toPrivateOutputDTO(Chat chat) {
        return PrivateChatOutputDTO.builder()
                .user1Id(chat.getUser1Id())
                .user2Id(chat.getUser2Id())
                .createdAt(chat.getCreatedAt())
                .build();
    }
}
