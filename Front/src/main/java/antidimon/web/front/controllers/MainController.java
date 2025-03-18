package antidimon.web.front.controllers;


import antidimon.web.front.security.JwtProvider;
import antidimon.web.front.services.inner.FrontUserService;
import io.grpc.StatusException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class MainController {

    private FrontUserService frontUserService;
    private JwtProvider jwtProvider;

    @GetMapping("/")
    public String mainPage() {
        return "main";
    }

    @GetMapping("/profile")
    public String profilePage(Model model, HttpServletRequest request) {
        String username = jwtProvider.getUsername(request.getCookies());
        try {
            model.addAttribute("user", frontUserService.getUser(username));
            model.addAttribute("id", frontUserService.getUserId(username));
        }catch (StatusException e){
            return "redirect:/";
        }
        return "profile";
    }

}
