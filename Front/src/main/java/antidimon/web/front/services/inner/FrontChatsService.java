package antidimon.web.front.services.inner;

import antidimon.web.front.models.dto.chats.ChatOutputDTO;
import antidimon.web.front.models.enums.ChatType;
import antidimon.web.front.services.grpc.MessageServiceClient;
import io.grpc.StatusRuntimeException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class FrontChatsService {

    private FrontUserService frontUserService;
    private MessageServiceClient messageServiceClient;

    public List<ChatOutputDTO> getUserChats(long userId) throws StatusRuntimeException {
        var chats = messageServiceClient.getUserChats(userId);

        for (ChatOutputDTO chat : chats) {
            if (chat.getChatType().equals(ChatType.PRIVATE)){
                if (chat.getUser1Id() == userId) chat.setName(frontUserService.getUsername(chat.getUser2Id()));
                else chat.setName(frontUserService.getUsername(chat.getUser1Id()));
            }
        }

        return chats;
    }
}
