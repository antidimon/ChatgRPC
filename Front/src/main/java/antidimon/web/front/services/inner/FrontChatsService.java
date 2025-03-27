package antidimon.web.front.services.inner;

import antidimon.web.front.models.dto.chats.*;
import antidimon.web.front.models.dto.users.ChatUserIdUsernameDTO;
import antidimon.web.front.models.enums.ChatType;
import antidimon.web.front.services.grpc.MessageServiceClient;
import io.grpc.StatusException;
import io.grpc.StatusRuntimeException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.rmi.ServerException;
import java.util.List;

@Service
@AllArgsConstructor
public class FrontChatsService {

    private FrontUserService frontUserService;
    private MessageServiceClient messageServiceClient;

    public List<ChatOutputDTO> getUserChats(long userId) throws StatusException {
        var chats = messageServiceClient.getUserChats(userId);

        for (ChatOutputDTO chat : chats) {
            if (chat.getChatType().equals(ChatType.PRIVATE)){
                if (chat.getUser1Id() == userId) chat.setName(frontUserService.getUsername(chat.getUser2Id()));
                else chat.setName(frontUserService.getUsername(chat.getUser1Id()));
            }
        }

        return chats;
    }

    public ChatToFrontDTO getChat(long chatId, boolean isPrivate) throws StatusException {
        if (isPrivate){
            PrivateChatOutputDTO chatDTO = this.getPrivateChat(chatId);
            return this.convertPrivateDTOToIdUsername(chatDTO);
        }else {
            GroupChatOutputDTO chatDTO = this.getGroupChat(chatId);
            return this.convertGroupDTOToIdUsername(chatDTO);
        }
    }

    private PrivateChatOutputDTO getPrivateChat(long chatId){
        return messageServiceClient.getPrivateChat(chatId);
    }

    private GroupChatOutputDTO getGroupChat(long chatId){
        return messageServiceClient.getGroupChat(chatId);
    }

    public PrivateChatWithIdUsernameDTO createPrivateChat(String creatorUsername, String username) throws StatusException {
        var chat = messageServiceClient.createPrivateChat(frontUserService.getUserId(creatorUsername),
                frontUserService.getUserId(username));
        return this.convertPrivateDTOToIdUsername(chat);
    }

    public GroupChatWithIdUsernamesDTO createGroupChat(String creatorUsername, GroupChatInputDTO groupChatInputDTO) throws StatusException {
        var chat = messageServiceClient.createGroupChat(frontUserService.getUserId(creatorUsername), groupChatInputDTO);
        return this.convertGroupDTOToIdUsername(chat);
    }

    public void deleteChat(String username, long chatId, boolean isPrivate) throws StatusException, SecurityException {
        long senderId = frontUserService.getUserId(username);
        if (isPrivate){
            PrivateChatOutputDTO chat = this.getPrivateChat(chatId);
            if (senderId == chat.getUser1Id() || senderId == chat.getUser2Id()){
                messageServiceClient.deletePrivateChat(chatId);
            }else {
                throw new SecurityException("Not your chat");
            }
        }else {
            GroupChatOutputDTO chat = this.getGroupChat(chatId);
            if (senderId == chat.getOwnerId()){
                messageServiceClient.deleteGroupChat(chatId);
            }else {
                throw new SecurityException("You are not owner");
            }
        }

    }


    private PrivateChatWithIdUsernameDTO convertPrivateDTOToIdUsername(PrivateChatOutputDTO chatDTO){
        return PrivateChatWithIdUsernameDTO.builder()
                .chatId(chatDTO.getChatId())
                .user1(new ChatUserIdUsernameDTO(chatDTO.getUser1Id(), frontUserService.getUsername(chatDTO.getUser1Id())))
                .user2(new ChatUserIdUsernameDTO(chatDTO.getUser2Id(), frontUserService.getUsername(chatDTO.getUser2Id())))
                .createdAt(chatDTO.getCreatedAt())
                .build();
    }

    private GroupChatWithIdUsernamesDTO convertGroupDTOToIdUsername(GroupChatOutputDTO chatDTO){
        return GroupChatWithIdUsernamesDTO.builder()
                .chatId(chatDTO.getChatId())
                .name(chatDTO.getName())
                .description(chatDTO.getDescription())
                .ownerId(chatDTO.getOwnerId())
                .users(chatDTO.getMembersIds().stream().map(id -> new ChatUserIdUsernameDTO(id, frontUserService.getUsername(id))).toList())
                .createdAt(chatDTO.getCreatedAt())
                .build();
    }
}
