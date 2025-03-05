package antidimon.web.front.services.inner;

import antidimon.web.front.models.dto.chats.ChatOutputDTO;
import antidimon.web.front.models.dto.messages.ChatMessageOutputDTO;
import antidimon.web.front.models.dto.messages.FrontMessageDTO;
import antidimon.web.front.models.enums.ChatType;
import antidimon.web.front.models.enums.MessageType;
import antidimon.web.front.services.grpc.MessageServiceClient;
import io.grpc.StatusException;
import io.grpc.StatusRuntimeException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class FrontChatsService {

    private FrontUserService frontUserService;
    private MessageServiceClient messageServiceClient;

    public List<FrontMessageDTO> getMessagesByChat(long chatId, String username) {

        var messages = messageServiceClient.getChatMessages(chatId);
        var messagesDTO = new ArrayList<FrontMessageDTO>();

        for (ChatMessageOutputDTO message : messages) {
            String senderUsername = frontUserService.getUsername(message.getSenderId());
            FrontMessageDTO messageDTO = FrontMessageDTO.builder()
                    .id(message.getId())
                    .senderId(message.getSenderId())
                    .senderUsername(senderUsername)
                    .message(message.getMessage())
                    .messageType((username.equals(senderUsername) ? MessageType.SENT : MessageType.RECEIVED))
                    .createdAt(message.getCreatedAt())
                    .build();
            messagesDTO.add(messageDTO);
        }

        return messagesDTO;
    }

    public List<ChatOutputDTO> getUserChats(long userId) throws StatusRuntimeException {
        var chats = messageServiceClient.getUserChats(userId);

        for (ChatOutputDTO chat : chats) {
            if (chat.getChatType().equals(ChatType.PRIVATE)){
                if (chat.getUser1Id() == userId) chat.setName(frontUserService.getUsername(chat.getUser2Id()));
                else chat.setName(frontUserService.getUsername(chat.getUser1Id()));
            }
        }

        return chats;
    }
}
