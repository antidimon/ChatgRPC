package antidimon.web.userservice.services.inner;


import antidimon.web.userservice.mapper.ChatUserMapper;
import antidimon.web.userservice.models.ChatUser;
import antidimon.web.userservice.models.dto.ChatUserIdUsernameDTO;
import antidimon.web.userservice.models.dto.ChatUserInputDTO;
import antidimon.web.userservice.models.dto.ChatUserOutputDTO;
import antidimon.web.userservice.repositories.ChatUserRepository;
import antidimon.web.userservice.services.grpc.MessageServiceClient;
import antidimon.web.userservice.services.grpc.NotificationServiceClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class ChatUserService {

    private NotificationServiceClient notificationServiceClient;
    private ChatUserRepository chatUserRepository;
    private ChatUserMapper chatUserMapper;
    private MessageServiceClient messageServiceClient;

    private ChatUser getUserEntity(String username) throws NoSuchElementException {
        Optional<ChatUser> chatUser = chatUserRepository.findByUsername(username);
        if(chatUser.isPresent()) return chatUser.get();
        throw new NoSuchElementException("User not found");
    }

    private ChatUser getUserEntity(long id) throws NoSuchElementException {
        Optional<ChatUser> chatUser = chatUserRepository.findById(id);
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

    public ChatUserOutputDTO getChatUserDTO(long userId) throws NoSuchElementException {
        Optional<ChatUser> user = chatUserRepository.findById(userId);
        if(user.isPresent()){
            return chatUserMapper.toDTO(user.get());
        }
        throw new NoSuchElementException("User not found");
    }

    @Transactional
    protected long saveUser(ChatUser chatUser) throws DataIntegrityViolationException {
        chatUserRepository.save(chatUser);
        notificationServiceClient.createSubscriptions(chatUser.getId());
        return chatUser.getId();
    }

    @Transactional
    public long saveUser(ChatUserInputDTO chatUserInputDTO) throws DataIntegrityViolationException {
        ChatUser user = chatUserMapper.toEntity(chatUserInputDTO);
        return this.saveUser(user);
    }

    @Transactional
    public void editChatUser(long id, ChatUserInputDTO editUserDTO) throws NoSuchElementException, DataIntegrityViolationException {
        ChatUser chatUser = this.getUserEntity(id);

        if (!editUserDTO.getUsername().isBlank()) chatUser.setUsername(editUserDTO.getUsername());
        if (!editUserDTO.getName().isBlank()) chatUser.setName(editUserDTO.getName());
        if (editUserDTO.getAge() != null) chatUser.setAge(editUserDTO.getAge());
        if (!editUserDTO.getEmail().isBlank()) chatUser.setEmail(editUserDTO.getEmail());
        if (!editUserDTO.getPhoneNumber().isBlank()) chatUser.setPhoneNumber(editUserDTO.getPhoneNumber());

        this.saveUser(chatUser);
    }

    @Transactional
    public long deleteUser(String username) throws NoSuchElementException {
        ChatUser chatUser = this.getUserEntity(username);
        log.info("Deleting user {}", chatUser.getId());
        chatUserRepository.delete(chatUser);
        messageServiceClient.deleteUserFromChats(chatUser.getId());
        notificationServiceClient.deleteAllInfo(chatUser.getId());

        return chatUser.getId();
    }

    public long getUserId(String username) throws NoSuchElementException{
        ChatUser chatUser = this.getUserEntity(username);
        return chatUser.getId();
    }

    public List<ChatUserIdUsernameDTO> getChatUsersByRegex(String regex) {

        List<ChatUser> users;
        if (regex == null){
            users = chatUserRepository.findAll();
        }else {
            users = chatUserRepository.findAllByUsernameRegex(regex);
        }

        return users.stream().map(chatUserMapper::toIdUsernameDTO).toList();
    }
}
