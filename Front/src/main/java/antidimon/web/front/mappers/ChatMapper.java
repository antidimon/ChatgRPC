package antidimon.web.front.mappers;

import antidimon.web.front.models.dto.chats.*;
import antidimon.web.front.models.dto.users.ChatUserIdUsernameDTO;
import antidimon.web.front.models.enums.ChatType;
import antidimon.web.front.services.inner.FrontUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@AllArgsConstructor
public class ChatMapper {

    private FrontUserService frontUserService;

    public ChatOutputDTO convertPrivateDTOToChatOutputDTO(PrivateChatOutputDTO chatDTO, String name){
        return ChatOutputDTO.builder()
                .chatId(chatDTO.getChatId())
                .name(name)
                .chatType(ChatType.PRIVATE)
                .user1Id(chatDTO.getUser1Id())
                .user2Id(chatDTO.getUser2Id())
                .createdAt(chatDTO.getCreatedAt())
                .build();
    }

    public ChatOutputDTO convertGroupDTOToChatOutputDTO(GroupChatOutputDTO chatDTO){
        return ChatOutputDTO.builder()
                .chatId(chatDTO.getChatId())
                .name(chatDTO.getName())
                .chatType(ChatType.GROUP)
                .ownerId(chatDTO.getOwnerId())
                .createdAt(chatDTO.getCreatedAt())
                .build();
    }


    public ChatToFrontDTO convertGroupDTOToIdUsername(GroupChatOutputDTO chatDTO) {
        return GroupChatWithIdUsernamesDTO.builder()
                .chatId(chatDTO.getChatId())
                .name(chatDTO.getName())
                .description(chatDTO.getDescription())
                .ownerId(chatDTO.getOwnerId())
                .users(chatDTO.getMembersIds().stream().map(id -> new ChatUserIdUsernameDTO(id, frontUserService.getUsername(id))).toList())
                .createdAt(chatDTO.getCreatedAt())
                .build();
    }

    public ChatToFrontDTO convertPrivateDTOToIdUsername(PrivateChatOutputDTO chatDTO) {
        return PrivateChatWithIdUsernameDTO.builder()
                .chatId(chatDTO.getChatId())
                .user1(new ChatUserIdUsernameDTO(chatDTO.getUser1Id(), frontUserService.getUsername(chatDTO.getUser1Id())))
                .user2(new ChatUserIdUsernameDTO(chatDTO.getUser2Id(), frontUserService.getUsername(chatDTO.getUser2Id())))
                .createdAt(chatDTO.getCreatedAt())
                .build();
    }


}
