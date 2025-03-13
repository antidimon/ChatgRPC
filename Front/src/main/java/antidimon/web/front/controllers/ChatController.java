package antidimon.web.front.controllers;


import antidimon.web.front.models.dto.chats.ChatOutputDTO;
import antidimon.web.front.models.dto.messages.ChatMessageOutputDTO;
import antidimon.web.front.models.dto.messages.FrontMessageDTO;
import antidimon.web.front.security.JwtProvider;
import antidimon.web.front.services.grpc.MessageServiceClient;
import antidimon.web.front.services.inner.FrontChatsService;
import antidimon.web.front.services.inner.FrontUserService;
import io.grpc.StatusRuntimeException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/chats")
@AllArgsConstructor
@Slf4j
public class ChatController {

    private MessageServiceClient messageServiceClient;

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
        } catch (StatusRuntimeException statusRuntimeException) {
            log.warn("Got no chats {}", statusRuntimeException.getCause().getMessage());
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok(chats);

    }
}
