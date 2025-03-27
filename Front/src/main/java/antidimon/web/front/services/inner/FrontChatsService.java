package antidimon.web.front.services.inner;

import antidimon.web.front.mappers.ChatMapper;
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
    private ChatMapper chatMapper;

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
            return chatMapper.convertPrivateDTOToIdUsername(chatDTO);
        }else {
            GroupChatOutputDTO chatDTO = this.getGroupChat(chatId);
            return chatMapper.convertGroupDTOToIdUsername(chatDTO);
        }
    }

    private PrivateChatOutputDTO getPrivateChat(long chatId){
        return messageServiceClient.getPrivateChat(chatId);
    }

    private GroupChatOutputDTO getGroupChat(long chatId){
        return messageServiceClient.getGroupChat(chatId);
    }

    public ChatOutputDTO createPrivateChat(String creatorUsername, String username) throws StatusException {
        var chat = messageServiceClient.createPrivateChat(frontUserService.getUserId(creatorUsername),
                frontUserService.getUserId(username));
        return chatMapper.convertPrivateDTOToChatOutputDTO(chat, username);
    }

    public ChatOutputDTO createGroupChat(String creatorUsername, GroupChatInputDTO groupChatInputDTO) throws StatusException {
        var chat = messageServiceClient.createGroupChat(frontUserService.getUserId(creatorUsername), groupChatInputDTO);
        return chatMapper.convertGroupDTOToChatOutputDTO(chat);
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
}
