package antidimon.web.front.services.grpc;


import antidimon.web.notificationservice.proto.NotificationServiceGrpc;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NotificationServiceClient {

    @GrpcClient("notification-service-client")
    private NotificationServiceGrpc.NotificationServiceBlockingStub notificationStub;

}
