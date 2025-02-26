package antidimon.web.messageservice.services.inner;


import antidimon.web.messageservice.mappers.ChatMapper;
import antidimon.web.messageservice.mappers.ChatMessageMapper;
import antidimon.web.messageservice.models.Chat;
import antidimon.web.messageservice.models.ChatMessage;
import antidimon.web.messageservice.models.dto.message.ChatMessageDTO;
import antidimon.web.messageservice.models.dto.message.ChatMessageOutputDTO;
import antidimon.web.messageservice.repositories.ChatMessageRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ChatMessageService {
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

    @Transactional
    public void deleteMessages(Chat chat) {
        chatMessageRepository.deleteChatMessagesByChat(chat);
    }
}
