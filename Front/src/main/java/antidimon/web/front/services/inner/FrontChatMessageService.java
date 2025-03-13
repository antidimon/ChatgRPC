package antidimon.web.front.services.inner;


import antidimon.web.front.models.dto.messages.ChatMessageDTO;
import antidimon.web.front.models.dto.messages.ChatMessageInputDTO;
import antidimon.web.front.models.dto.messages.ChatMessageOutputDTO;
import antidimon.web.front.models.dto.messages.FrontMessageDTO;
import antidimon.web.front.models.enums.MessageType;
import antidimon.web.front.services.grpc.MessageServiceClient;
import io.grpc.StatusException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class FrontChatMessageService {

    private MessageServiceClient messageServiceClient;
    private FrontUserService frontUserService;


    public List<FrontMessageDTO> getMessagesByChat(long chatId, String username) throws StatusException {

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

    @Transactional
    public void createMessage(long chatId, String username, ChatMessageInputDTO chatMessageInputDTO) throws StatusException {

        var userId = frontUserService.getUserId(username);
        ChatMessageDTO chatMessageDTO = ChatMessageDTO.builder()
                .chatId(chatId)
                .senderId(userId)
                .message(chatMessageInputDTO.getMessage())
                .build();

        messageServiceClient.createMessage(chatMessageDTO);
    }
}
