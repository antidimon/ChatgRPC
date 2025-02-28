package antidimon.web.messageservice.services.inner;


import antidimon.web.messageservice.mappers.ChatMapper;
import antidimon.web.messageservice.mappers.ChatMessageMapper;
import antidimon.web.messageservice.models.Chat;
import antidimon.web.messageservice.models.ChatMessage;
import antidimon.web.messageservice.models.ChatType;
import antidimon.web.messageservice.models.dto.message.ChatMessageDTO;
import antidimon.web.messageservice.models.dto.message.ChatMessageOutputDTO;
import antidimon.web.messageservice.repositories.ChatMessageRepository;
import antidimon.web.messageservice.services.grpc.NotificationServiceClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ChatMessageService {

    private NotificationServiceClient notificationServiceClient;
    private ChatMapper chatMapper;
    private ChatMessageMapper chatMessageMapper;
    private ChatMessageRepository chatMessageRepository;
    private ChatService chatService;

    @Transactional
    protected void saveMessage(ChatMessage message) {
        chatMessageRepository.save(message);
    }

    @Transactional
    public void saveMessage(ChatMessageDTO chatMessageDTO) throws NoSuchElementException {
        Chat chat = chatService.getChat(chatMessageDTO.getChatId());
        ChatMessage msg = chatMapper.toEntity(chatMessageDTO, chat);
        this.saveMessage(msg);
        this.chooseMessageNotification(chat, chatMessageDTO.getSenderId());
    }

    public ChatMessage getMessage(long messageId) throws NoSuchElementException {
        Optional<ChatMessage> message = chatMessageRepository.findById(messageId);
        if (message.isPresent()) return message.get();
        throw new NoSuchElementException();
    }

    public List<ChatMessageOutputDTO> getChatMessages(long chatId) throws NoSuchElementException {
        Chat chat = chatService.getChat(chatId);
        return chat.getMessages().stream().map(chatMessageMapper::toDTO).toList();
    }

    @Transactional
    public void updateMessage(long messageId, String text) throws NoSuchElementException {
        ChatMessage message = this.getMessage(messageId);
        message.setMessage(text);
        this.saveMessage(message);
    }

    @Transactional
    public void deleteMessage(long messageId) throws NoSuchElementException{
        chatMessageRepository.deleteById(messageId);
    }



    private void chooseMessageNotification(Chat chat, long senderId) {
        if (chat.getType().equals(ChatType.PRIVATE)){
            if (chat.getUser1Id() == senderId) {
                notificationServiceClient.sendMessageNotification(chat.getUser2Id(),
                        "New message in private chat " + chat.getId());
            }else {
                notificationServiceClient.sendMessageNotification(chat.getUser1Id(),
                        "New message in private chat " + chat.getId());
            }
        }else {
            var listOfIds = chat.getMembers().stream().map(member -> member.getId().getUserId())
                    .filter(id -> id != senderId).toList();
            listOfIds.forEach(id -> notificationServiceClient.sendMessageNotification(id,
                    "New message in group chat " + chat.getId()));
        }
    }

    @Transactional
    public void deleteUserMessagesFromGroupChats(long userId) {
        this.chatMessageRepository.deleteAllBySenderId(userId);
    }
}
