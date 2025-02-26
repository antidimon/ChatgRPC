package antidimon.web.userservice.services.inner;

import antidimon.web.userservice.models.dto.ChatUserInputDTO;
import antidimon.web.userservice.repositories.ChatUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ValidationService {

    private ChatUserRepository chatUserRepository;

    public boolean isUsernameValid(String username) {
        return chatUserRepository.findByUsername(username).isEmpty();
    }

    public boolean isEmailValid(String email) {
        return  chatUserRepository.findByEmail(email).isEmpty();
    }

    public boolean isPhoneNumberValid(String phoneNumber) {
        return  chatUserRepository.findByPhoneNumber(phoneNumber).isEmpty();
    }

    public List<String> checkNewUserForErrors(ChatUserInputDTO chatUser) {
        List<String> errors = new ArrayList<>();
        if (!isUsernameValid(chatUser.getUsername())) errors.add("username");
        if (!isEmailValid(chatUser.getEmail())) errors.add("email");
        if (!isPhoneNumberValid(chatUser.getPhoneNumber())) errors.add("phoneNumber");
        if (chatUser.getAge() >= 100 || chatUser.getAge() <= 0) errors.add("age");
        return errors;
    }
}
