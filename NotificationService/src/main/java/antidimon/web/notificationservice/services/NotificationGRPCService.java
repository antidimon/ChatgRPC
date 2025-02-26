package antidimon.web.notificationservice.services;


import antidimon.web.notificationservice.models.dto.NotificationOutputDTO;
import antidimon.web.notificationservice.proto.GetNotificationsResponse;
import antidimon.web.notificationservice.proto.SendNotificationRequest;
import antidimon.web.notificationservice.proto.SendNotificationResponse;
import antidimon.web.notificationservice.proto.Notif;
import io.grpc.Status;
import io.grpc.StatusException;
import io.grpc.stub.StreamObserver;
import lombok.AllArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;

@GrpcService
@AllArgsConstructor
public class NotificationGRPCService extends antidimon.web.notificationservice.proto.NotificationServiceGrpc.NotificationServiceImplBase {

    private NotificationService notificationService;

    @Override
    public void sendNotification(SendNotificationRequest request, StreamObserver<SendNotificationResponse> responseObserver) {
        try {
            notificationService.sendNotification(request.getUserId(), request.getType(), request.getMessage());

            SendNotificationResponse resp = SendNotificationResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage("Notification sent successfully")
                    .build();

            responseObserver.onNext(resp);
            responseObserver.onCompleted();

        }catch (Exception e){
            responseObserver.onError(new StatusException(Status.INTERNAL));
        }
    }

    @Override
    public void getNotifications(antidimon.web.notificationservice.proto.GetNotificationsRequest request, StreamObserver<GetNotificationsResponse> responseObserver) {
        try {
            List<NotificationOutputDTO> notifications = notificationService.getNotifications(request.getUserId());

            GetNotificationsResponse.Builder builder = GetNotificationsResponse.newBuilder();

            for (NotificationOutputDTO notificationOutputDTO : notifications){
                builder.addNotifications(Notif.newBuilder()
                        .setId(notificationOutputDTO.getId())
                        .setType(notificationOutputDTO.getType().toString())
                        .setMsg(notificationOutputDTO.getMsg())
                        .setCreatedAt(notificationOutputDTO.getCreatedAt().toString())
                        .build()
                );
            }

            GetNotificationsResponse resp = builder.build();

            responseObserver.onNext(resp);
            responseObserver.onCompleted();
        }catch (Exception e){
            responseObserver.onError(new StatusException(Status.INTERNAL));
        }
    }
}
