package antidimon.web.front.controllers;


import antidimon.web.front.models.dto.users.ChatUserIdUsernameDTO;
import antidimon.web.front.models.dto.users.ChatUserInputDTO;
import antidimon.web.front.models.dto.users.ChatUserOutputDTO;
import antidimon.web.front.security.JwtProvider;
import antidimon.web.front.services.inner.FrontUserService;
import io.grpc.StatusException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
@Slf4j
public class UserController {

    private FrontUserService frontUserService;
    private JwtProvider jwtProvider;

    @GetMapping
    public ResponseEntity<List<ChatUserIdUsernameDTO>> getUsers(HttpServletRequest request,
                                                                @RequestParam(name = "regex", required = false) String regex) {

        List<ChatUserIdUsernameDTO> users;
        String senderUsername = jwtProvider.getUsername(request.getCookies());
        try {
            users = frontUserService.searchUsers(regex, senderUsername);
        }catch (StatusException e){
            log.warn("Couldn't invoke users by " + regex + ". " + e);
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ChatUserOutputDTO> getUser(@PathVariable("userId") long userId){

        ChatUserOutputDTO user;
        try {
            user = frontUserService.getUser(userId);
        }catch (StatusException e) {
            log.warn("Couldn't invoke user by " + userId + ". " + e);
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok(user);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<?> editUser(HttpServletRequest request,
                                      @RequestBody ChatUserInputDTO chatUserInputDTO,
                                      @PathVariable("userId") long userId) {

        try {
            frontUserService.editUser(userId, chatUserInputDTO);
        }catch(Exception e) {
            log.warn("Couldn't update user. " + e);
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(HttpServletRequest request){
        String username = jwtProvider.getUsername(request.getCookies());
        try {
            frontUserService.deleteUser(username);
        }catch (Exception e){
            log.warn("Couldn't delete user " + username + ". " + e);
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok().build();
    }

}
