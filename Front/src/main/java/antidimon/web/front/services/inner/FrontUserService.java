package antidimon.web.front.services.inner;

import antidimon.web.front.models.dto.MyUserRegisterDTO;
import antidimon.web.front.models.dto.users.ChatUserIdUsernameDTO;
import antidimon.web.front.models.dto.users.ChatUserInputDTO;
import antidimon.web.front.models.dto.users.ChatUserOutputDTO;
import antidimon.web.front.repositories.MyUserRepository;
import antidimon.web.front.security.MyUser;
import antidimon.web.front.services.grpc.UserServiceClient;
import io.grpc.StatusException;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.directory.InvalidAttributesException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class FrontUserService {

    private MyUserRepository myUserRepository;
    private PasswordEncoder passwordEncoder;
    private UserServiceClient userServiceClient;

    @Transactional(rollbackFor = {InvalidAttributesException.class, InternalError.class})
    public void save(MyUserRegisterDTO user) throws InvalidAttributesException, InternalError, StatusException {
        user.setPassword(encodePersonPassword(user.getPassword()));

        myUserRepository.save(MyUser.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .build());

        var errors = userServiceClient.save(user);

        if (errors.isEmpty()) return;

        if (errors.getFirst().equals("Internal error")) throw new InternalError();
        else throw new InvalidAttributesException(String.join(",", errors));
    }

    private String encodePersonPassword(String password) {
        return passwordEncoder.encode(password);
    }

    public long getUserId(String username) throws NoSuchElementException {

        return myUserRepository.findByUsername(username).get().getId();
    }

    public String getUsername(long senderId) {
        return myUserRepository.findById(senderId).get().getUsername();
    }

    public List<ChatUserIdUsernameDTO> searchUsers(String regex, String senderUsername) throws StatusException {

        return userServiceClient.searchUsers(regex).stream().filter(user -> !user.getUsername().equals(senderUsername)).toList();
    }

    public ChatUserOutputDTO getUser(long userId) throws StatusException {
        String username = getUsername(userId);
        return this.getUser(username);
    }
    public ChatUserOutputDTO getUser(String username) throws StatusException {
        return userServiceClient.getUser(username);
    }

    @Transactional(rollbackFor = {StatusException.class})
    public void editUser(long userId, ChatUserInputDTO chatUserInputDTO) throws StatusException{
        if (!chatUserInputDTO.getPassword().isBlank()) {
            MyUser user = myUserRepository.findById(userId).get();
            if (chatUserInputDTO.getPassword() != null) user.setPassword(this.encodePersonPassword(chatUserInputDTO.getPassword()));
            myUserRepository.save(user);
        }
        userServiceClient.editUser(userId, chatUserInputDTO);
    }

    @Transactional(rollbackFor = {InternalError.class})
    public void deleteUser(String username) {

        myUserRepository.deleteByUsername(username);
        try {
            userServiceClient.deleteUser(username);
        }catch (StatusException e){
            throw new InternalError();
        }
    }
}
