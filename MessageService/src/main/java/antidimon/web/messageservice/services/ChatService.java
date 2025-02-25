package antidimon.web.messageservice.services;

import antidimon.web.messageservice.mappers.ChatMapper;
import antidimon.web.messageservice.models.Chat;
import antidimon.web.messageservice.models.ChatParticipant;
import antidimon.web.messageservice.models.ChatParticipantPK;
import antidimon.web.messageservice.models.ChatType;
import antidimon.web.messageservice.models.dto.chat.*;
import antidimon.web.messageservice.repositories.ChatParticipantRepository;
import antidimon.web.messageservice.repositories.ChatRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ChatService {
    private final ChatMapper chatMapper;
    private ChatRepository chatRepository;
    private ChatParticipantRepository chatParticipantRepository;

    public Chat getChat(long chatId) throws NoSuchElementException {
        Optional<Chat> chat = chatRepository.findById(chatId);
        if (chat.isPresent()) return chat.get();
        throw new NoSuchElementException("No chat with id " + chatId);
    }

    @Transactional
    public long createGroupChat(GroupChatInputDTO chatInputDTO) {
        Chat chat = chatMapper.toEntity(chatInputDTO);
        System.out.println(chat.getType());
        return chatRepository.save(chat).getId();
    }

    public GroupChatOutputDTO getGroupChat(long chatId) throws NoSuchElementException {
        Chat chat = this.getChat(chatId);
        return chatMapper.toGroupOutputDTO(chat);
    }

    @Transactional
    public void updateGroupChat(EditGroupChatDTO editGroupChatDTO) throws NoSuchElementException {
        Chat chat = this.getChat(editGroupChatDTO.getChatId());

        chat.setName(editGroupChatDTO.getName());
        chat.setDescription(editGroupChatDTO.getDescription());

        chatRepository.save(chat);
    }

    @Transactional
    public long createPrivateChat(long user1Id, long user2Id) {
        Chat chat = Chat.builder()
                .type(ChatType.PRIVATE)
                .user1Id(user1Id)
                .user2Id(user2Id)
                .build();

        return chatRepository.save(chat).getId();
    }

    public PrivateChatOutputDTO getPrivateChatMembers(long chatId) throws NoSuchElementException{
        Chat chat = this.getChat(chatId);
        return PrivateChatOutputDTO.builder()
                .user1Id(chat.getUser1Id())
                .user2Id(chat.getUser2Id())
                .createdAt(chat.getCreatedAt())
                .build();
    }

    @Transactional
    public void addUserToChat(long chatId, long userId) throws NoSuchElementException{
        Chat chat = this.getChat(chatId);

        ChatParticipant chatParticipant = new ChatParticipant(new ChatParticipantPK(chatId, userId), chat);
        chatParticipantRepository.save(chatParticipant);

        chat.getMembers().add(chatParticipant);

        chatRepository.save(chat);
    }

    @Transactional
    public void kickUserFromChat(long chatId, long userId) throws NoSuchElementException{
        ChatParticipantPK pk = new ChatParticipantPK(chatId, userId);
        ChatParticipant chatParticipant = chatParticipantRepository.findById(pk)
                .orElseThrow(() -> new NoSuchElementException("Участник чата не найден"));

        chatParticipantRepository.delete(chatParticipant);
    }

    public List<ChatOutputDTO> getUserChats(long userId) {
        List<Chat> chats = chatRepository.findUserPrivateAndOwnerChats(userId);
        chats.addAll(chatRepository.findUserMemberChats(userId));
        return chats.stream().map(chatMapper::toOutputDTO).toList();
    }

    @Transactional
    public void deletePrivateChat(long chatId) throws NoSuchElementException, IllegalArgumentException{
        Chat chat = this.getChat(chatId);
        if (chat.getType().equals(ChatType.PRIVATE)) chatRepository.delete(chat);
        throw new IllegalArgumentException();
    }

    @Transactional
    public void deleteGroupChat(long chatId) throws NoSuchElementException, IllegalArgumentException{
        Chat chat = this.getChat(chatId);
        if (chat.getType().equals(ChatType.GROUP)) chatRepository.delete(chat);
        throw new IllegalArgumentException();
    }
}
