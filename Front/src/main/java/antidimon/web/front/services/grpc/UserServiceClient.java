package antidimon.web.front.services.grpc;

import antidimon.web.front.models.dto.MyUserRegisterDTO;
import antidimon.web.userservice.proto.RegisterUserRequest;
import antidimon.web.userservice.proto.RegisterUserResponse;
import antidimon.web.userservice.proto.UserServiceGrpc;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
@Slf4j
public class UserServiceClient {

    @GrpcClient("user-service-client")
    private UserServiceGrpc.UserServiceBlockingStub userStub;


    public List<String> save(MyUserRegisterDTO user) throws StatusRuntimeException {

        RegisterUserRequest request = RegisterUserRequest.newBuilder()
                .setUsername(user.getUsername())
                .setName(user.getName())
                .setAge(user.getAge())
                .setEmail(user.getEmail())
                .setPhoneNumber(user.getPhoneNumber())
                .setPassword(user.getPassword())
                .build();
        try {
            RegisterUserResponse response = userStub.registerUser(request);
            if (response.getSuccess()) log.info("User registered successfully");
        }catch (StatusRuntimeException e){
            System.out.println(e.getStatus());
            if (e.getStatus().getCode().equals(Status.Code.INVALID_ARGUMENT)){
                return Arrays.stream(e.getStatus().getDescription().split(",")).toList();
            }else if (e.getStatus().getCode().equals(Status.Code.INTERNAL)){
                return List.of("Internal error");
            }
        }
        return Collections.emptyList();
    }
}
