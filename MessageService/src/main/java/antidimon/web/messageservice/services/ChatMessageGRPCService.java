package antidimon.web.messageservice.services;


import antidimon.web.messageservice.models.dto.message.ChatMessageDTO;
import antidimon.web.messageservice.models.dto.message.ChatMessageOutputDTO;
import antidimon.web.messageservice.proto.*;
import io.grpc.Status;
import io.grpc.StatusException;
import io.grpc.stub.StreamObserver;
import lombok.AllArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;
import java.util.NoSuchElementException;

@GrpcService
@AllArgsConstructor
public class ChatMessageGRPCService extends antidimon.web.messageservice.proto.MessageServiceGrpc.MessageServiceImplBase {
    private ChatMessageService chatMessageService;
    private ChatService chatService;

    @Override
    public void registerMessage(RegisterMessageRequest request, StreamObserver<RegisterMessageResponse> responseObserver) {
        ChatMessageDTO msg = ChatMessageDTO.builder()
                .chatId(request.getChatId())
                .message(request.getText())
                .senderId(request.getSenderId())
                .build();
        try {
            chatMessageService.saveMessage(msg);
            RegisterMessageResponse response = RegisterMessageResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage("Message registered successfully")
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }catch (NoSuchElementException e){
            responseObserver.onError(new StatusException(Status.NOT_FOUND.withDescription("No chat found")));
        }catch (Exception e){
            responseObserver.onError(new StatusException(Status.INTERNAL));
        }
    }

    @Override
    public void getMessagesByChat(GetMessagesByChatRequest request, StreamObserver<GetMessagesByChatResponse> responseObserver) {
        try{
            List<ChatMessageOutputDTO> messages = chatMessageService.getChatMessages(request.getChatId());
            GetMessagesByChatResponse.Builder responseBuilder = GetMessagesByChatResponse.newBuilder();
            for (ChatMessageOutputDTO msg : messages) {
                responseBuilder.addMessages(
                        Msg.newBuilder()
                                .setMessageId(msg.getId())
                                .setSenderId(msg.getSenderID())
                                .setText(msg.getMessage())
                                .setCreatedAt(msg.getCreatedAt().toString())
                                .build()
                );
            }
            GetMessagesByChatResponse response = responseBuilder
                    .setSuccess(true)
                    .setMessage("Messages retrieved successfully")
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }catch (NoSuchElementException e){
            responseObserver.onError(new StatusException(Status.NOT_FOUND.withDescription("No chat found")));
        }catch (Exception e){
            responseObserver.onError(new StatusException(Status.INTERNAL));
        }
    }

    @Override
    public void updateMessage(antidimon.web.messageservice.proto.UpdateMessageRequest request, StreamObserver<UpdateMessageResponse> responseObserver) {
        try{
            chatMessageService.updateMessage(request.getMessageId(), request.getText());
            UpdateMessageResponse response = UpdateMessageResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage("Updated successfully")
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }catch (NoSuchElementException e){
            responseObserver.onError(new StatusException(Status.NOT_FOUND.withDescription("No message found")));
        }catch (Exception e){
            responseObserver.onError(new StatusException(Status.INTERNAL));
        }
    }

    @Override
    public void deleteMessage(DeleteMessageRequest request, StreamObserver<DeleteMessageResponse> responseObserver) {
        try {
            chatMessageService.deleteMessage(request.getMessageId());
            DeleteMessageResponse response = DeleteMessageResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage("Deleted successfully")
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }catch (NoSuchElementException e){
            responseObserver.onError(new StatusException(Status.NOT_FOUND.withDescription("No message found")));
        }catch (Exception e){
            responseObserver.onError(new StatusException(Status.INTERNAL));
        }
    }
}
