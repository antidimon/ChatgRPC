package antidimon.web.front.services.grpc;

import antidimon.web.front.models.dto.MyUserRegisterDTO;
import antidimon.web.front.models.dto.users.ChatUserIdUsernameDTO;
import antidimon.web.front.models.dto.users.ChatUserOutputDTO;
import antidimon.web.userservice.proto.*;
import io.grpc.Status;
import io.grpc.StatusException;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
@Slf4j
public class UserServiceClient {

    @GrpcClient("user-service-client")
    private UserServiceGrpc.UserServiceBlockingStub userStub;


    public List<String> save(MyUserRegisterDTO user) throws StatusException {

        RegisterUserRequest request = RegisterUserRequest.newBuilder()
                .setUsername(user.getUsername())
                .setName(user.getName())
                .setAge(user.getAge())
                .setEmail(user.getEmail())
                .setPhoneNumber(user.getPhoneNumber())
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

    public List<ChatUserIdUsernameDTO> searchUsers(String regex) throws StatusException {

        SearchUsersByRegexRequest request = SearchUsersByRegexRequest.newBuilder()
                .setRegex(regex)
                .build();

        SearchUsersByRegexResponse response = userStub.searchUsersByRegex(request);

        return response.getUsersList().stream()
                .map(user -> new ChatUserIdUsernameDTO(user.getId(), user.getUsername())).toList();
    }

    public ChatUserOutputDTO getUser(String username) throws StatusException {

        GetUserRequest request = GetUserRequest.newBuilder().setUsername(username).build();
        GetUserResponse response = userStub.getUser(request);
        User user = response.getUser();

        return ChatUserOutputDTO.builder()
                .username(username)
                .name(user.getName())
                .age((short)user.getAge())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .createdAt(LocalDateTime.parse(user.getCreatedAt()))
                .build();

    }
}
