package antidimon.web.front.services.grpc;

import antidimon.web.messageservice.proto.ChatServiceGrpc;
import antidimon.web.messageservice.proto.MessageServiceGrpc;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MessageServiceClient {

    @GrpcClient("message-service-client")
    private MessageServiceGrpc.MessageServiceBlockingStub messageStub;

    @GrpcClient("message-service-client")
    private ChatServiceGrpc.ChatServiceBlockingStub chatsStub;
}
