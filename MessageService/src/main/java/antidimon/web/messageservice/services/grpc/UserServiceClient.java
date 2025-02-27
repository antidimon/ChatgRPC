package antidimon.web.messageservice.services.grpc;

import antidimon.web.userservice.proto.GetUserByIdRequest;
import antidimon.web.userservice.proto.GetUserByIdResponse;
import antidimon.web.userservice.proto.UserServiceGrpc;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserServiceClient {

    @GrpcClient("user-service-client")
    private UserServiceGrpc.UserServiceBlockingStub userStub;

    public boolean isUserExist(long userId) {

        GetUserByIdRequest request = GetUserByIdRequest.newBuilder().setUserId(userId).build();

        try {
            GetUserByIdResponse response = userStub.getUserById(request);
            return true;
        }catch (StatusRuntimeException e){
            return false;
        }
    }
}
