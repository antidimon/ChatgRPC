package antidimon.web.front.services.grpc;

import antidimon.web.front.models.dto.chats.ChatOutputDTO;
import antidimon.web.front.models.dto.messages.ChatMessageDTO;
import antidimon.web.front.models.dto.messages.ChatMessageOutputDTO;
import antidimon.web.front.models.dto.messages.FrontMessageDTO;
import antidimon.web.front.models.enums.ChatType;
import antidimon.web.messageservice.proto.*;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class MessageServiceClient {

    @GrpcClient("message-service-client")
    private MessageServiceGrpc.MessageServiceBlockingStub messageStub;

    @GrpcClient("message-service-client")
    private ChatServiceGrpc.ChatServiceBlockingStub chatsStub;



    public List<ChatOutputDTO> getUserChats(long userId) throws StatusRuntimeException {

        GetUserChatsRequest request = GetUserChatsRequest.newBuilder()
                .setUserId(userId)
                .build();

        GetUserChatsResponse response = chatsStub.getUserChats(request);
        return response.getChatsList().stream()
                .map(chat -> ChatOutputDTO.builder()
                    .chatId(chat.getChatId())
                    .chatType(ChatType.valueOf(chat.getType()))
                    .name(chat.getName())
                    .ownerId(chat.getOwnerId())
                    .user1Id(chat.getUser1Id())
                    .user2Id(chat.getUser2Id())
                    .build())
                .toList();
    }

    public List<ChatMessageOutputDTO> getChatMessages(long chatId) throws StatusRuntimeException {

        GetMessagesByChatRequest request = GetMessagesByChatRequest.newBuilder()
                .setChatId(chatId)
                .build();

        GetMessagesByChatResponse response = messageStub.getMessagesByChat(request);
        return response.getMessagesList().stream()
                .map(message -> ChatMessageOutputDTO.builder()
                        .id(message.getMessageId())
                        .senderId(message.getSenderId())
                        .message(message.getText())
                        .build())
                .toList();
    }


    public void createMessage(ChatMessageDTO chatMessageDTO) {

        RegisterMessageRequest request = RegisterMessageRequest.newBuilder()
                .setChatId(chatMessageDTO.getChatId())
                .setSenderId(chatMessageDTO.getSenderId())
                .setText(chatMessageDTO.getMessage())
                .build();

        RegisterMessageResponse response = messageStub.registerMessage(request);

    }
}
