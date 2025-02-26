package antidimon.web.userservice.services.inner;


import antidimon.web.userservice.mapper.ChatUserMapper;
import antidimon.web.userservice.models.ChatUser;
import antidimon.web.userservice.models.dto.ChatUserInputDTO;
import antidimon.web.userservice.models.dto.ChatUserOutputDTO;
import antidimon.web.userservice.repositories.ChatUserRepository;
import antidimon.web.userservice.services.grpc.MessageServiceClient;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ChatUserService {

    private ChatUserRepository chatUserRepository;
    private ChatUserMapper chatUserMapper;
    private MessageServiceClient messageServiceClient;

    private ChatUser getUserEntity(String username) throws NoSuchElementException {
        Optional<ChatUser> chatUser = chatUserRepository.findByUsername(username);
        if(chatUser.isPresent()) return chatUser.get();
        throw new NoSuchElementException("User not found");
    }

    public ChatUserOutputDTO getChatUserDTO(String username) throws NoSuchElementException {
        Optional<ChatUser> user = chatUserRepository.findByUsername(username);
        if(user.isPresent()){
            return chatUserMapper.toDTO(user.get());
        }
        throw new NoSuchElementException("User not found");
    }

    @Transactional
    protected long saveUser(ChatUser chatUser) throws DataIntegrityViolationException {
        chatUserRepository.save(chatUser);
        return chatUser.getId();
    }

    @Transactional
    public long saveUser(ChatUserInputDTO chatUserInputDTO) throws DataIntegrityViolationException {
        ChatUser user = chatUserMapper.toEntity(chatUserInputDTO);
        return this.saveUser(user);
    }

    @Transactional
    public void editChatUser(ChatUserInputDTO editUserDTO) throws NoSuchElementException, DataIntegrityViolationException {
        ChatUser chatUser = this.getUserEntity(editUserDTO.getUsername());

        chatUser.setUsername(editUserDTO.getUsername());
        chatUser.setName(editUserDTO.getName());
        chatUser.setAge(editUserDTO.getAge());
        chatUser.setEmail(editUserDTO.getEmail());
        chatUser.setPhoneNumber(editUserDTO.getPhoneNumber());
        chatUser.setPassword(editUserDTO.getPassword());

        this.saveUser(chatUser);
    }

    @Transactional
    public long deleteUser(String username) throws NoSuchElementException {
        ChatUser chatUser = this.getUserEntity(username);
        chatUserRepository.delete(chatUser);

        messageServiceClient.deleteUserFromChats(chatUser.getId());

        return chatUser.getId();
    }

    public long getUserId(String username) throws NoSuchElementException{
        ChatUser chatUser = this.getUserEntity(username);
        return chatUser.getId();
    }
}
