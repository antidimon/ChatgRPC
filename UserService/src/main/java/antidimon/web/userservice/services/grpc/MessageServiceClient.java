package antidimon.web.userservice.services.grpc;

import antidimon.web.messageservice.proto.ChatServiceGrpc;
import antidimon.web.messageservice.proto.DeleteUserChatsRequest;
import antidimon.web.messageservice.proto.DeleteUserChatsResponse;
import antidimon.web.messageservice.proto.MessageServiceGrpc;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MessageServiceClient {

    @GrpcClient("message-service-client")
    private MessageServiceGrpc.MessageServiceBlockingStub messagesStub;

    @GrpcClient("message-service-client")
    private ChatServiceGrpc.ChatServiceBlockingStub chatsStub;

    public void deleteUserFromChats(long userId) throws StatusRuntimeException {
        DeleteUserChatsRequest request = DeleteUserChatsRequest.newBuilder().setUserId(userId).build();

        DeleteUserChatsResponse response = chatsStub.deleteUserChats(request);

        if (response.getSuccess()){
            log.info("Getted success response from messageService (deleteUserFromChats)");
        }

    }

}
