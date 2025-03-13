package antidimon.web.front.controllers;


import antidimon.web.front.models.dto.messages.ChatMessageInputDTO;
import antidimon.web.front.models.dto.messages.FrontMessageDTO;
import antidimon.web.front.security.JwtProvider;
import antidimon.web.front.services.inner.FrontChatMessageService;
import io.grpc.StatusException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/chats/{chatId}/messages")
@Slf4j
public class MessageController {

    private FrontChatMessageService frontChatMessageService;
    private JwtProvider jwtProvider;


    @GetMapping
    public ResponseEntity<List<FrontMessageDTO>> getChatMessages(@PathVariable("chatId") long chatId, HttpServletRequest request) {
        List<FrontMessageDTO> chatMessages;
        log.info("Request for chats");
        try {
            String username = jwtProvider.getUsername(request.getCookies());
            chatMessages = frontChatMessageService.getMessagesByChat(chatId, username);
        } catch (StatusException e) {
            log.warn("Got no chat messages {}", e.getCause().getMessage());
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok(chatMessages);
    }

    @PostMapping
    public ResponseEntity<String> createMessage(@PathVariable("chatId") long chatId,
                                                @RequestBody ChatMessageInputDTO chatMessageInputDTO, HttpServletRequest request) {

        log.info("Request to create chat message");
        try {
            String username = jwtProvider.getUsername(request.getCookies());
            frontChatMessageService.createMessage(chatId, username, chatMessageInputDTO);
        }catch (StatusException e){
            log.warn("Couldn't create message. {}", e.getCause().getMessage());
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok("Success");


    }
}
