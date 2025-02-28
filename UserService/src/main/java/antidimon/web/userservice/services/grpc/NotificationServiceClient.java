package antidimon.web.userservice.services.grpc;

import antidimon.web.notificationservice.proto.*;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NotificationServiceClient {

    @GrpcClient("notification-service-client")
    private NotificationServiceGrpc.NotificationServiceBlockingStub notificationStub;

    @GrpcClient("notification-service-client")
    private SubscriptionServiceGrpc.SubscriptionServiceBlockingStub subscriptionStub;

    public void deleteAllInfo(long userId){
        DeleteAllSubscriptionsRequest request1 = DeleteAllSubscriptionsRequest.newBuilder().setUserId(userId).build();

        DeleteAllSubscriptionsResponse response1 = subscriptionStub.deleteAllSubscriptions(request1);
        if (response1.getSuccess()){
            log.info("Successfully deleted all subscriptions");
        }

        DeleteAllNotificationsRequest request2 = DeleteAllNotificationsRequest.newBuilder().setUserId(userId).build();

        DeleteAllNotificationsResponse response2 = notificationStub.deleteAllNotifications(request2);
        if (response2.getSuccess()){
            log.info("Successfully deleted all notifications");
        }
    }


    public void createSubscriptions(long id) {

        CreateAllSubscriptionsRequest request = CreateAllSubscriptionsRequest.newBuilder().setUserId(id).build();

        CreateAllSubscriptionsResponse response = subscriptionStub.createAllSubscriptions(request);
        if (response.getSuccess()){
            log.info("Successfully created subscriptions");
        }
    }
}
