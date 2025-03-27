package antidimon.web.messageservice.services.grpc;

import antidimon.web.messageservice.models.ChatType;
import antidimon.web.messageservice.models.dto.chat.*;
import antidimon.web.messageservice.proto.*;
import antidimon.web.messageservice.services.inner.ChatMessageService;
import antidimon.web.messageservice.services.inner.ChatService;
import io.grpc.Status;
import io.grpc.StatusException;
import io.grpc.stub.StreamObserver;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;
import java.util.NoSuchElementException;

@GrpcService
@AllArgsConstructor
@Slf4j
public class ChatGRPCService extends antidimon.web.messageservice.proto.ChatServiceGrpc.ChatServiceImplBase {
    private ChatService chatService;
    private ChatMessageService chatMessageService;

    @Override
    public void createGroupChat(CreateGroupChatRequest request, StreamObserver<CreateGroupChatResponse> responseObserver) {
        GroupChatInputDTO chatInputDTO = GroupChatInputDTO.builder()
                .ownerId(request.getOwnerId())
                .name(request.getName())
                .description(request.getDescription())
                .build();

        try {
            var chat = chatService.createGroupChat(chatInputDTO);
            CreateGroupChatResponse response = CreateGroupChatResponse.newBuilder()
                    .setChat(convertGroupToGRPC(chat))
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        }catch (NoSuchElementException ex){
            responseObserver.onError(new StatusException(Status.NOT_FOUND.withDescription(ex.getMessage())));
        }catch (Exception e){
            log.error("Couldn't create group chat", e);
            responseObserver.onError(new StatusException(Status.INTERNAL));
        }
    }

    @Override
    public void getGroupChat(GetGroupChatRequest request, StreamObserver<GetGroupChatResponse> responseObserver) {
        try {
            GroupChatOutputDTO chat = chatService.getGroupChat(request.getChatId());
            GetGroupChatResponse response = GetGroupChatResponse.newBuilder()
                    .setChat(convertGroupToGRPC(chat))
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        }catch (NoSuchElementException noSuchElementException){
            responseObserver.onError(new StatusException(Status.NOT_FOUND.withDescription("Couldn't find chat")));
        }catch (Exception e){
            log.error("Couldn't get group chat", e);
            responseObserver.onError(new StatusException(Status.INTERNAL));
        }
    }

    @Override
    public void updateGroupChat(antidimon.web.messageservice.proto.UpdateGroupChatRequest request, StreamObserver<UpdateGroupChatResponse> responseObserver) {
        EditGroupChatDTO editGroupChatDTO = EditGroupChatDTO.builder()
                .chatId(request.getChatId())
                .name(request.getName())
                .description(request.getDescription())
                .build();

        try {
            var chat = chatService.updateGroupChat(editGroupChatDTO);
            UpdateGroupChatResponse response = UpdateGroupChatResponse.newBuilder()
                    .setChat(convertGroupToGRPC(chat))
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        }catch(NoSuchElementException noSuchElementException){
            responseObserver.onError(new StatusException(Status.NOT_FOUND.withDescription("Couldn't find chat")));
        } catch (Exception e) {
            log.error("Couldn't update group chat", e);
            responseObserver.onError(new StatusException(Status.INTERNAL));
        }
    }

    @Override
    public void deleteGroupChat(DeleteGroupChatRequest request, StreamObserver<DeleteGroupChatResponse> responseObserver) {
        try {
            chatService.deleteGroupChat(request.getChatId());
            DeleteGroupChatResponse response = DeleteGroupChatResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage("Deleted successfully")
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (NoSuchElementException noSuchElementException) {
            responseObserver.onError(new StatusException(Status.NOT_FOUND.withDescription("Couldn't find chat")));
        } catch (IllegalArgumentException illegalArgumentException){
            responseObserver.onError(new StatusException(Status.INVALID_ARGUMENT.withDescription("Wrong chat type")));
        } catch (Exception e) {
            log.error("Couldn't delete group chat", e);
            responseObserver.onError(new StatusException(Status.INTERNAL));
        }
    }

    @Override
    public void createPrivateChat(CreatePrivateChatRequest request, StreamObserver<CreatePrivateChatResponse> responseObserver) {
        try {
            var chat = chatService.createPrivateChat(request.getCreatorId(), request.getUser2Id());

            CreatePrivateChatResponse response = CreatePrivateChatResponse.newBuilder()
                    .setChat(convertPrivateToGRPC(chat))
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        }catch (NoSuchElementException ex){
            log.error("Users not found");
            responseObserver.onError(new StatusException(Status.NOT_FOUND.withDescription(ex.getMessage())));
        } catch (Exception e) {
            log.error("Couldn't create private chat", e);
            responseObserver.onError(new StatusException(Status.INTERNAL));
        }
    }

    @Override
    public void getPrivateChat(GetPrivateChatRequest request, StreamObserver<GetPrivateChatResponse> responseObserver) {
        try {
            var chat = chatService.getPrivateChat(request.getChatId());
            GetPrivateChatResponse response = GetPrivateChatResponse.newBuilder()
                    .setChat(convertPrivateToGRPC(chat))
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch(NoSuchElementException noSuchElementException){
            responseObserver.onError(new StatusException(Status.NOT_FOUND.withDescription("Couldn't find chat")));
        } catch (Exception e) {
            log.error("Couldn't get private chat", e);
            responseObserver.onError(new StatusException(Status.INTERNAL));
        }
    }

    @Override
    public void deletePrivateChat(DeletePrivateChatRequest request, StreamObserver<DeletePrivateChatResponse> responseObserver) {
        try {
            chatService.deletePrivateChat(request.getChatId());
            DeletePrivateChatResponse response = DeletePrivateChatResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage("Deleted successfully")
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        }catch(NoSuchElementException noSuchElementException){
            responseObserver.onError(new StatusException(Status.NOT_FOUND.withDescription("Couldn't find chat")));
        } catch (IllegalArgumentException illegalArgumentException){
            responseObserver.onError(new StatusException(Status.INVALID_ARGUMENT.withDescription("Wrong chat type")));
        } catch (Exception e) {
            log.error("Couldn't delete private chat", e);
            responseObserver.onError(new StatusException(Status.INTERNAL));
        }
    }

    @Override
    public void addUserToGroupChat(AddUserToGroupChatRequest request, StreamObserver<AddUserToGroupChatResponse> responseObserver) {
        try {
            chatService.addUserToChat(request.getChatId(), request.getUserId());
            AddUserToGroupChatResponse response = AddUserToGroupChatResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage("Added successfully")
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        }catch (NoSuchElementException noSuchElementException){
            responseObserver.onError(new StatusException(Status.NOT_FOUND.withDescription(noSuchElementException.getMessage())));
        }catch (Exception e){
            log.error("Couldn't add user to group chat", e);
            responseObserver.onError(new StatusException(Status.INTERNAL));
        }
    }

    @Override
    public void removeUserFromGroupChat(RemoveUserFromGroupChatRequest request, StreamObserver<RemoveUserFromGroupChatResponse> responseObserver) {
        try {
            chatService.kickUserFromGroupChat(request.getChatId(), request.getUserId());
            RemoveUserFromGroupChatResponse response = RemoveUserFromGroupChatResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage("Removed successfully")
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        }catch (NoSuchElementException noSuchElementException){
            responseObserver.onError(new StatusException(Status.NOT_FOUND.withDescription(noSuchElementException.getMessage())));
        }catch (Exception e){
            log.error("Couldn't remove user from group chat", e);
            responseObserver.onError(new StatusException(Status.INTERNAL));
        }
    }

    @Override
    public void getUserChats(GetUserChatsRequest request, StreamObserver<GetUserChatsResponse> responseObserver) {
        try {
            List<ChatOutputDTO> chats = chatService.getUserChatsDTO(request.getUserId());
            GetUserChatsResponse.Builder getUserChatsResponseBuilder = GetUserChatsResponse.newBuilder();
            for (ChatOutputDTO chatOutputDTO : chats) {
                ChatInfo chat;
                if (chatOutputDTO.getChatType().equals(ChatType.PRIVATE)){
                    chat = ChatInfo.newBuilder()
                            .setChatId(chatOutputDTO.getChatId())
                            .setType(chatOutputDTO.getChatType().toString())
                            .setUser1Id(chatOutputDTO.getUser1Id())
                            .setUser2Id(chatOutputDTO.getUser2Id())
                            .build();
                }else {
                    chat = ChatInfo.newBuilder()
                            .setChatId(chatOutputDTO.getChatId())
                            .setName(chatOutputDTO.getName())
                            .setType(chatOutputDTO.getChatType().toString())
                            .setOwnerId(chatOutputDTO.getOwnerId())
                            .build();
                }
                getUserChatsResponseBuilder.addChats(chat);
            }
            GetUserChatsResponse getUserChatsResponse = getUserChatsResponseBuilder.build();

            responseObserver.onNext(getUserChatsResponse);
            responseObserver.onCompleted();

        }catch (NoSuchElementException noSuchElementException){
            responseObserver.onError(new StatusException(Status.NOT_FOUND.withDescription("Couldn't find chats")));
        }catch (Exception e){
            log.error("Couldn't get user chats", e);
            responseObserver.onError(new StatusException(Status.INTERNAL));
        }
    }

    @Override
    public void deleteUserChats(DeleteUserChatsRequest request, StreamObserver<DeleteUserChatsResponse> responseObserver) {
        try {
            chatService.deleteUserChats(request.getUserId());
            chatMessageService.deleteUserMessagesFromGroupChats(request.getUserId());

            DeleteUserChatsResponse response = DeleteUserChatsResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage("Deleted successfully")
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }catch (Exception e){
            log.error("Couldn't delete user chats", e);
            responseObserver.onError(new StatusException(Status.INTERNAL));
        }
    }


    private static PrivateChat convertPrivateToGRPC(PrivateChatOutputDTO privateChatOutputDTO){
        return PrivateChat.newBuilder()
                .setChatId(privateChatOutputDTO.getChatId())
                .setUser1Id(privateChatOutputDTO.getUser1Id())
                .setUser2Id(privateChatOutputDTO.getUser2Id())
                .setCreatedAt(privateChatOutputDTO.getCreatedAt().toString())
                .build();
    }

    private static GroupChat convertGroupToGRPC(GroupChatOutputDTO groupChatOutputDTO){
        return GroupChat.newBuilder()
                .setChatId(groupChatOutputDTO.getChatId())
                .setName(groupChatOutputDTO.getName())
                .setDescription(groupChatOutputDTO.getDescription())
                .setOwnerId(groupChatOutputDTO.getOwnerId())
                .addAllParticipants(groupChatOutputDTO.getMembersIds())
                .setCreatedAt(groupChatOutputDTO.getCreatedAt().toString())
                .build();
    }
}
