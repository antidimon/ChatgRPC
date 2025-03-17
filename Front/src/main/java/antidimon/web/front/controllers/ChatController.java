package antidimon.web.front.controllers;


import antidimon.web.front.models.dto.chats.ChatIdResponse;
import antidimon.web.front.models.dto.chats.ChatOutputDTO;
import antidimon.web.front.models.dto.chats.ChatToFrontDTO;
import antidimon.web.front.models.dto.chats.GroupChatInputDTO;
import antidimon.web.front.models.dto.messages.ChatMessageOutputDTO;
import antidimon.web.front.models.dto.messages.FrontMessageDTO;
import antidimon.web.front.security.JwtProvider;
import antidimon.web.front.services.grpc.MessageServiceClient;
import antidimon.web.front.services.inner.FrontChatsService;
import antidimon.web.front.services.inner.FrontUserService;
import io.grpc.StatusException;
import io.grpc.StatusRuntimeException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/chats")
@AllArgsConstructor
@Slf4j
public class ChatController {

    private FrontUserService frontUserService;
    private FrontChatsService frontChatsService;
    private JwtProvider jwtProvider;

    @GetMapping
    public ResponseEntity<List<ChatOutputDTO>> getUserChats(HttpServletRequest request) {
        List<ChatOutputDTO> chats;
        try {
            long userId = frontUserService.getUserId(jwtProvider.getUsername(request.getCookies()));
            chats = frontChatsService.getUserChats(userId);

            log.info("Got user chats {}", userId);

        } catch (NoSuchElementException noSuchElementException) {
            log.warn("Got no user chats {}", noSuchElementException.getCause().getMessage());
            return ResponseEntity.notFound().build();
        } catch (StatusException statusException) {
            log.warn("Got no chats {}", statusException.getCause().getMessage());
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok(chats);
    }

    @GetMapping("/{chatId}")
    public ResponseEntity<?> getChatInfo(HttpServletRequest request, @PathVariable("chatId") long chatId,
                                         @RequestParam("isPrivate") boolean isPrivate) {
        ChatToFrontDTO chat;
        try {
            chat = frontChatsService.getChat(chatId, isPrivate);
        }catch (StatusException statusException){
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok(chat);
    }

    @PostMapping("/private")
    public ResponseEntity<?> createPrivateChat(HttpServletRequest request, @RequestParam("user2") String username) {

        String creatorUsername = jwtProvider.getUsername(request.getCookies());
        long chatId;
        try {
            chatId = frontChatsService.createPrivateChat(creatorUsername, username);
        } catch (Exception e) {
            log.warn("Got status exception while creating private chat", e);
            return ResponseEntity.internalServerError().build();
        }
        ChatIdResponse response = new ChatIdResponse();
        response.setId(chatId);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/group")
    public ResponseEntity<?> createGroupChat(HttpServletRequest request, @RequestBody GroupChatInputDTO groupChatInputDTO) {

        String creatorUsername = jwtProvider.getUsername(request.getCookies());
        long chatId;
        try {
            chatId = frontChatsService.createGroupChat(creatorUsername, groupChatInputDTO);
        }catch (Exception e){
            log.warn("Got status exception while creating group chat", e);
            return ResponseEntity.internalServerError().build();
        }
        ChatIdResponse response = new ChatIdResponse();
        response.setId(chatId);

        return ResponseEntity.ok(response);
    }
}
