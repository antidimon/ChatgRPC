package antidimon.web.messageservice.services.grpc;


import antidimon.web.notificationservice.proto.NotificationServiceGrpc;
import antidimon.web.notificationservice.proto.SendNotificationRequest;
import antidimon.web.notificationservice.proto.SendNotificationResponse;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NotificationServiceClient {

    @GrpcClient("notification-service-client")
    private NotificationServiceGrpc.NotificationServiceBlockingStub notificationStub;


    public void sendChatNotification(long user_id, String message){

        SendNotificationRequest request = SendNotificationRequest.newBuilder()
                .setType("CHAT")
                .setMessage(message)
                .setUserId(user_id)
                .build();

        try {
            SendNotificationResponse response = notificationStub.sendNotification(request);
            if (response.getSuccess()){
                log.info("Notification sent successfully");
            }
        }catch (StatusRuntimeException e){
            log.error("Notification service error (new chat)", e);
        }

    }


    public void sendMessageNotification(long user_id, String message){

        SendNotificationRequest request = SendNotificationRequest.newBuilder()
                .setType("MESSAGE")
                .setMessage(message)
                .setUserId(user_id)
                .build();

        try {
            SendNotificationResponse response = notificationStub.sendNotification(request);
            if (response.getSuccess()){
                log.info("Notification sent successfully");
            }
        }catch (StatusRuntimeException e){
            log.error("Notification service error (new message)", e);
        }

    }


}
