package antidimon.web.front.controllers;


import antidimon.web.front.models.dto.users.ChatUserIdUsernameDTO;
import antidimon.web.front.models.dto.users.ChatUserOutputDTO;
import antidimon.web.front.services.inner.FrontUserService;
import io.grpc.StatusException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
@Slf4j
public class UserController {

    private FrontUserService frontUserService;

    @GetMapping
    public ResponseEntity<List<ChatUserIdUsernameDTO>> getUsers(@RequestParam(name = "regex", required = false) String regex) {

        List<ChatUserIdUsernameDTO> users;
        try {
            users = frontUserService.searchUsers(regex);
        }catch (StatusException e){
            log.warn("Couldn't invoke users by " + regex + ". " + e);
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok(users);

    }

}
