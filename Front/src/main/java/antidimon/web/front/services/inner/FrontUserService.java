package antidimon.web.front.services.inner;

import antidimon.web.front.models.dto.MyUserRegisterDTO;
import antidimon.web.front.models.dto.users.ChatUserIdUsernameDTO;
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

@Service
@AllArgsConstructor
public class FrontUserService {

    private MyUserRepository myUserRepository;
    private PasswordEncoder passwordEncoder;
    private UserServiceClient userServiceClient;

    @Transactional(rollbackFor = {InvalidAttributesException.class, InternalError.class})
    public void save(MyUserRegisterDTO user) throws InvalidAttributesException, InternalError, StatusException {
        encodePersonPassword(user);

        myUserRepository.save(MyUser.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .build());

        var errors = userServiceClient.save(user);

        if (errors.isEmpty()) return;

        if (errors.getFirst().equals("Internal error")) throw new InternalError();
        else throw new InvalidAttributesException(String.join(",", errors));
    }

    private void encodePersonPassword(MyUserRegisterDTO user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
    }

    public long getUserId(String username) throws NoSuchElementException {

        return myUserRepository.findByUsername(username).get().getId();
    }

    public String getUsername(long senderId) {
        return myUserRepository.findById(senderId).get().getUsername();
    }

    public List<ChatUserIdUsernameDTO> searchUsers(String regex) throws StatusException {

        return userServiceClient.searchUsers(regex);
    }
}
