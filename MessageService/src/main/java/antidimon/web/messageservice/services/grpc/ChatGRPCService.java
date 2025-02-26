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
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;
import java.util.NoSuchElementException;

@GrpcService
@AllArgsConstructor
public class ChatGRPCService extends antidimon.web.messageservice.proto.ChatServiceGrpc.ChatServiceImplBase {
    private ChatService chatService;

    @Override
    public void createGroupChat(CreateGroupChatRequest request, StreamObserver<CreateGroupChatResponse> responseObserver) {
        GroupChatInputDTO chatInputDTO = GroupChatInputDTO.builder()
                .ownerId(request.getOwnerId())
                .name(request.getName())
                .description(request.getDescription())
                .build();

        try {
            long chatId = chatService.createGroupChat(chatInputDTO);
            CreateGroupChatResponse response = CreateGroupChatResponse.newBuilder()
                    .setSuccess(true)
                    .setChatId(chatId)
                    .setMessage("Chat created successfully")
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception e){
            responseObserver.onError(new StatusException(Status.INTERNAL));
        }
    }

    @Override
    public void getGroupChat(GetGroupChatRequest request, StreamObserver<GetGroupChatResponse> responseObserver) {
        try {
            GroupChatOutputDTO chat = chatService.getGroupChat(request.getChatId());
            GetGroupChatResponse response = GetGroupChatResponse.newBuilder()
                    .setChatId(chat.getChatId())
                    .setName(chat.getName())
                    .setDescription(chat.getDescription())
                    .setOwnerId(chat.getOwnerId())
                    .addAllParticipants(chat.getMembersIds())
                    .setCreatedAt(chat.getCreatedAt().toString())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        }catch (NoSuchElementException noSuchElementException){
            responseObserver.onError(new StatusException(Status.NOT_FOUND.withDescription("Couldn't find chat")));
        }catch (Exception e){
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
            chatService.updateGroupChat(editGroupChatDTO);
            UpdateGroupChatResponse response = UpdateGroupChatResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage("Updated successfully")
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        }catch(NoSuchElementException noSuchElementException){
            responseObserver.onError(new StatusException(Status.NOT_FOUND.withDescription("Couldn't find chat")));
        } catch (Exception e) {
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
            responseObserver.onError(new StatusException(Status.INTERNAL));
        }
    }

    @Override
    public void createPrivateChat(CreatePrivateChatRequest request, StreamObserver<CreatePrivateChatResponse> responseObserver) {
        try {
            long chatId = chatService.createPrivateChat(request.getUser1Id(), request.getUser2Id());

            CreatePrivateChatResponse response = CreatePrivateChatResponse.newBuilder()
                    .setSuccess(true)
                    .setChatId(chatId)
                    .setMessage("Private chat created successfully")
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception e) {
            responseObserver.onError(new StatusException(Status.INTERNAL));
        }
    }

    @Override
    public void getPrivateChat(GetPrivateChatRequest request, StreamObserver<GetPrivateChatResponse> responseObserver) {
        try {
            PrivateChatOutputDTO chatOutputDTO = chatService.getPrivateChatMembers(request.getChatId());
            GetPrivateChatResponse response = GetPrivateChatResponse.newBuilder()
                    .setId(request.getChatId())
                    .setUser1Id(chatOutputDTO.getUser1Id())
                    .setUser2Id(chatOutputDTO.getUser2Id())
                    .setCreatedAt(chatOutputDTO.getCreatedAt().toString())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch(NoSuchElementException noSuchElementException){
            responseObserver.onError(new StatusException(Status.NOT_FOUND.withDescription("Couldn't find chat")));
        } catch (Exception e) {
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
            responseObserver.onError(new StatusException(Status.NOT_FOUND.withDescription("Couldn't find chat")));
        }catch (Exception e){
            e.printStackTrace();
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
            responseObserver.onError(new StatusException(Status.NOT_FOUND.withDescription("Couldn't find chat")));
        }catch (Exception e){
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
            e.printStackTrace();
            responseObserver.onError(new StatusException(Status.INTERNAL));
        }
    }

    @Override
    public void deleteUserChats(DeleteUserChatsRequest request, StreamObserver<DeleteUserChatsResponse> responseObserver) {
        try {
            chatService.deleteUserChats(request.getUserId());

            DeleteUserChatsResponse response = DeleteUserChatsResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage("Deleted successfully")
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }catch (Exception e){
            responseObserver.onError(new StatusException(Status.INTERNAL));
        }
    }
}
