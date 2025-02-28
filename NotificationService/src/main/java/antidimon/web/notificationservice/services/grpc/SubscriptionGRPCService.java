package antidimon.web.notificationservice.services.grpc;


import antidimon.web.notificationservice.models.SubscriptionType;
import antidimon.web.notificationservice.proto.*;
import antidimon.web.notificationservice.services.inner.SubscriptionService;
import io.grpc.Status;
import io.grpc.StatusException;
import io.grpc.stub.StreamObserver;
import lombok.AllArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.Map;

@GrpcService
@AllArgsConstructor
public class SubscriptionGRPCService extends antidimon.web.notificationservice.proto.SubscriptionServiceGrpc.SubscriptionServiceImplBase {

    private SubscriptionService subscriptionService;

    @Override
    public void subscribeToNotifications(SubscribeToNotificationsRequest request, StreamObserver<SubscribeToNotificationsResponse> responseObserver) {
        try {
            subscriptionService.subscribe(request.getUserId(), request.getType());

            SubscribeToNotificationsResponse response = SubscribeToNotificationsResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage("Subscribed")
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        }catch (Exception e){
            responseObserver.onError(new StatusException(Status.INTERNAL));
        }
    }

    @Override
    public void unsubscribeFromNotifications(UnsubscribeFromNotificationsRequest request, StreamObserver<UnsubscribeFromNotificationsResponse> responseObserver) {
        try {
            subscriptionService.unSubscribe(request.getUserId(), request.getType());

            UnsubscribeFromNotificationsResponse response = UnsubscribeFromNotificationsResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage("Subscribed")
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        }catch (Exception e){
            responseObserver.onError(new StatusException(Status.INTERNAL));
        }
    }

    @Override
    public void getUserSubscriptions(antidimon.web.notificationservice.proto.GetUserSubscriptionsRequest request, StreamObserver<GetUserSubscriptionsResponse> responseObserver) {
        try {
            Map<SubscriptionType, Boolean> flags = subscriptionService.getSubscriptionStatuses(request.getUserId());

            GetUserSubscriptionsResponse response = GetUserSubscriptionsResponse.newBuilder()
                    .setMessages(flags.get(SubscriptionType.MESSAGE))
                    .setChats(flags.get(SubscriptionType.CHAT))
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        }catch (Exception e){
            responseObserver.onError(new StatusException(Status.INTERNAL));
        }
    }

    @Override
    public void deleteAllSubscriptions(DeleteAllSubscriptionsRequest request, StreamObserver<DeleteAllSubscriptionsResponse> responseObserver) {
        try {
            this.subscriptionService.deleteAllSubscriptions(request.getUserId());

            DeleteAllSubscriptionsResponse response = DeleteAllSubscriptionsResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage("Subscriptions deleted")
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        }catch (Exception e){
            responseObserver.onError(new StatusException(Status.INTERNAL));
        }
    }

    @Override
    public void createAllSubscriptions(CreateAllSubscriptionsRequest request, StreamObserver<CreateAllSubscriptionsResponse> responseObserver) {
        try {
            this.subscriptionService.createAllSubscriptions(request.getUserId());

            CreateAllSubscriptionsResponse response = CreateAllSubscriptionsResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage("Subscriptions created")
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        }catch (Exception e){
            responseObserver.onError(new StatusException(Status.INTERNAL));
        }
    }
}
