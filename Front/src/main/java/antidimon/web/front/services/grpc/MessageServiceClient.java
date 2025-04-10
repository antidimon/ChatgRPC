package antidimon.web.front.services.grpc;

import antidimon.web.front.models.dto.chats.*;
import antidimon.web.front.models.dto.messages.ChatMessageDTO;
import antidimon.web.front.models.dto.messages.ChatMessageOutputDTO;
import antidimon.web.front.models.dto.messages.FrontMessageDTO;
import antidimon.web.front.models.enums.ChatType;
import antidimon.web.messageservice.proto.*;
import io.grpc.StatusException;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

import java.rmi.ServerException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class MessageServiceClient {

    @GrpcClient("message-service-client")
    private MessageServiceGrpc.MessageServiceBlockingStub messageStub;

    @GrpcClient("message-service-client")
    private ChatServiceGrpc.ChatServiceBlockingStub chatsStub;



    public List<ChatOutputDTO> getUserChats(long userId) {

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

    public PrivateChatOutputDTO getPrivateChat(long chatId) {
        GetPrivateChatRequest request = GetPrivateChatRequest.newBuilder().setChatId(chatId).build();
        GetPrivateChatResponse response = chatsStub.getPrivateChat(request);
        var chat = response.getChat();
        return PrivateChatOutputDTO.builder()
                .chatId(chat.getId())
                .user1Id(chat.getUser1Id())
                .user2Id(chat.getUser2Id())
                .createdAt(LocalDateTime.parse(chat.getCreatedAt()))
                .build();
    }

    public GroupChatOutputDTO getGroupChat(long chatId) {
        GetGroupChatRequest request = GetGroupChatRequest.newBuilder().setChatId(chatId).build();
        GetGroupChatResponse response = chatsStub.getGroupChat(request);
        var chat = response.getChat();
        return GroupChatOutputDTO.builder()
                .chatId(chat.getChatId())
                .name(chat.getName())
                .description(chat.getDescription())
                .ownerId(chat.getOwnerId())
                .membersIds(chat.getParticipantsList())
                .createdAt(LocalDateTime.parse(chat.getCreatedAt()))
                .build();
    }

    public List<ChatMessageOutputDTO> getChatMessages(long chatId) {

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
        if (!response.getSuccess()) log.error("Failed to register message. {}", response.getMessage());

    }

    public PrivateChatOutputDTO createPrivateChat(long creatorId, long user2Id) {

        CreatePrivateChatRequest request = CreatePrivateChatRequest.newBuilder().setCreatorId(creatorId).setUser2Id(user2Id).build();
        CreatePrivateChatResponse response = chatsStub.createPrivateChat(request);
        var chat = response.getChat();
        return PrivateChatOutputDTO.builder()
                .chatId(chat.getId())
                .user1Id(chat.getUser1Id())
                .user2Id(chat.getUser2Id())
                .createdAt(LocalDateTime.parse(chat.getCreatedAt()))
                .build();


    }

    public GroupChatOutputDTO createGroupChat(long userId, GroupChatInputDTO groupChatInputDTO) {

        CreateGroupChatRequest request = CreateGroupChatRequest.newBuilder()
                .setOwnerId(userId)
                .setName(groupChatInputDTO.getName())
                .setDescription(groupChatInputDTO.getDescription())
                .build();
        CreateGroupChatResponse response = chatsStub.createGroupChat(request);
        var chat = response.getChat();
        return GroupChatOutputDTO.builder()
                .chatId(chat.getChatId())
                .name(chat.getName())
                .description(chat.getDescription())
                .ownerId(chat.getOwnerId())
                .membersIds(chat.getParticipantsList())
                .createdAt(LocalDateTime.parse(chat.getCreatedAt()))
                .build();
    }

    public void deletePrivateChat(long chatId) {
        DeletePrivateChatResponse response = chatsStub.deletePrivateChat(
                DeletePrivateChatRequest.newBuilder().setChatId(chatId).build());
        if (!response.getSuccess()) log.error("Failed to delete private chat. {}", response.getMessage());
    }

    public void deleteGroupChat(long chatId) {
        DeleteGroupChatResponse response = chatsStub.deleteGroupChat(
                DeleteGroupChatRequest.newBuilder().setChatId(chatId).build());
        if (!response.getSuccess()) log.error("Failed to delete group chat. {}", response.getMessage());
    }
}
