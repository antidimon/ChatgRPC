package antidimon.web.front.services.inner;

import antidimon.web.front.models.dto.MyUserRegisterDTO;
import antidimon.web.front.repositories.MyUserRepository;
import antidimon.web.front.security.MyUser;
import antidimon.web.front.services.grpc.UserServiceClient;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.directory.InvalidAttributesException;

@Service
@AllArgsConstructor
public class AuthService {

    private MyUserRepository myUserRepository;
    private PasswordEncoder passwordEncoder;
    private UserServiceClient userServiceClient;

    @Transactional(rollbackFor = {InvalidAttributesException.class, InternalError.class})
    public void save(MyUserRegisterDTO user) throws InvalidAttributesException, InternalError {
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
}
