package antidimon.web.userservice.services.grpc;

import antidimon.web.userservice.models.dto.ChatUserInputDTO;
import antidimon.web.userservice.models.dto.ChatUserOutputDTO;
import antidimon.web.userservice.proto.*;
import antidimon.web.userservice.services.inner.ChatUserService;
import antidimon.web.userservice.services.inner.ValidationService;
import io.grpc.StatusException;
import io.grpc.stub.StreamObserver;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

import io.grpc.Status;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.NoSuchElementException;


@GrpcService
@AllArgsConstructor
@Slf4j
public class UserGRPCService extends UserServiceGrpc.UserServiceImplBase {

    private ValidationService validationService;
    private ChatUserService chatUserService;

    @Override
    public void registerUser(RegisterUserRequest request,
                             StreamObserver<RegisterUserResponse> responseObserver) {

        ChatUserInputDTO inputDTO = ChatUserInputDTO.builder()
                .username(request.getUsername())
                .name(request.getName())
                .age((short) request.getAge())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .password(request.getPassword())
                .build();
        List<String> errors = validationService.checkNewUserForErrors(inputDTO);
        if (errors.isEmpty()) {
            try {
                long id = chatUserService.saveUser(inputDTO);

                log.info("Successfully registered new user with id {}", id);

                RegisterUserResponse response = RegisterUserResponse.newBuilder()
                        .setSuccess(true)
                        .setMessage("User registered successfully")
                        .build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
            }catch (DataIntegrityViolationException dataIntegrityViolationException) {
                responseObserver.onError(new StatusException(Status.INTERNAL.withDescription("Failed to save user")));
            }
        }else {
            String errorMessage = String.join(", ", errors);
            responseObserver.onError(new StatusException(Status.INVALID_ARGUMENT.withDescription(errorMessage)));
        }

    }

    @Override
    public void getUser(GetUserRequest request,
                        StreamObserver<GetUserResponse> responseObserver) {
        try {
            ChatUserOutputDTO chatUser = chatUserService.getChatUserDTO(request.getUsername());
            GetUserResponse response = GetUserResponse.newBuilder()
                    .setUser(User.newBuilder()
                            .setUsername(chatUser.getUsername())
                            .setName(chatUser.getName())
                            .setAge(chatUser.getAge())
                            .setEmail(chatUser.getEmail())
                            .setPhoneNumber(chatUser.getPhoneNumber())
                            .setCreatedAt(chatUser.getCreatedAt().toString())
                    )
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        }catch (NoSuchElementException e) {
            responseObserver.onError(new StatusException(Status.NOT_FOUND.withDescription("User not found")));
        }

    }

    @Override
    public void getUserById(GetUserByIdRequest request, StreamObserver<GetUserByIdResponse> responseObserver) {
        try {
            ChatUserOutputDTO chatUser = chatUserService.getChatUserDTO(request.getUserId());
            GetUserByIdResponse response = GetUserByIdResponse.newBuilder()
                    .setUser(User.newBuilder()
                            .setUsername(chatUser.getUsername())
                            .setName(chatUser.getName())
                            .setAge(chatUser.getAge())
                            .setEmail(chatUser.getEmail())
                            .setPhoneNumber(chatUser.getPhoneNumber())
                            .setCreatedAt(chatUser.getCreatedAt().toString())
                    )
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        }catch (NoSuchElementException e) {
            responseObserver.onError(new StatusException(Status.NOT_FOUND.withDescription("User not found")));
        }
    }

    @Override
    public void updateUser(UpdateUserRequest request,
                           StreamObserver<UpdateUserResponse> responseObserver) {

        ChatUserInputDTO editUserDTO = ChatUserInputDTO.builder()
                .username(request.getUsername())
                .name(request.getName())
                .age((short)request.getAge())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .password(request.getPassword())
                .build();

        try {
            chatUserService.editChatUser(editUserDTO);
            UpdateUserResponse response = UpdateUserResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage("User updated successfully")
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        }catch (DataIntegrityViolationException dataIntegrityViolationException) {
            responseObserver.onError(new StatusException(Status.INTERNAL.withDescription("Failed to update user")));
        }catch (NoSuchElementException e) {
            responseObserver.onError(new StatusException(Status.NOT_FOUND.withDescription("User not found")));
        }


    }

    @Override
    public void deleteUser(DeleteUserRequest request,
                           StreamObserver<DeleteUserResponse> responseObserver) {

        try{
            long id = chatUserService.deleteUser(request.getUsername());

            log.info("Successfully deleted user with id {}", id);

            DeleteUserResponse response = DeleteUserResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage("User deleted successfully")
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        }catch (NoSuchElementException e){
            responseObserver.onError(new StatusException(Status.NOT_FOUND.withDescription("User not found")));
        }catch (Exception e){
            responseObserver.onError(new StatusException(Status.INTERNAL.withDescription("Failed to delete user")));
        }
    }
}
