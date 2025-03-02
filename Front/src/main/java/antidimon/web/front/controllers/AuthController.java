package antidimon.web.front.controllers;


import antidimon.web.front.models.dto.MyUserRegisterDTO;
import antidimon.web.front.services.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
@AllArgsConstructor
public class AuthController {

    private AuthService authService;

    @GetMapping("/login")
    public String login(@RequestParam(required = false, name = "error") String exception,
                        Model model, Authentication auth) {

        if (auth != null){
            return "redirect:/";
        }
        if (exception != null) {
            model.addAttribute("error", exception);
        }
        return "auth/login";
    }

    @GetMapping("/register")
    public String register(Model model, Authentication auth) {
        if (auth != null){return "redirect:/";}
        model.addAttribute("user", new MyUserRegisterDTO());
        return "auth/register";
    }

    @PostMapping("/register")
    public String doRegister(@ModelAttribute("user") @Valid MyUserRegisterDTO user, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "auth/register";
        }
        try {
            authService.save(user);

            return "redirect:/login";
        }catch (Exception e){
            return "redirect:/register";
        }
    }
}
