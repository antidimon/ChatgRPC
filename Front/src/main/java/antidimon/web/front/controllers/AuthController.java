package antidimon.web.front.controllers;


import antidimon.web.front.models.dto.MyUserRegisterDTO;
import antidimon.web.front.services.inner.AuthService;
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

import javax.naming.directory.InvalidAttributesException;
import java.util.Arrays;
import java.util.HashMap;

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
    public String register(Model model, Authentication auth, @RequestParam(required = false, name = "error") String error) {
        if (auth != null){return "redirect:/";}
        if (error != null) {
            model.addAttribute("error", error);
        }else {
            model.addAttribute("user", new MyUserRegisterDTO());
            var map = new HashMap<String, Boolean>();
            map.put("email", false);
            map.put("username", false);
            map.put("age", false);
            map.put("phoneNumber", false);
            model.addAttribute("errors", map);
        }
        return "auth/register";
    }

    @PostMapping("/register")
    public String doRegister(@ModelAttribute("user") @Valid MyUserRegisterDTO user, BindingResult bindingResult,
                             Model model) {

        if (bindingResult.hasErrors()) {
            return "auth/register";
        }
        try {
            authService.save(user);
        }catch (InvalidAttributesException invalidAttributesException){
            System.out.println("gg1");
            model.addAttribute("user", user);
            var map = new HashMap<String, Boolean>();
            for (String error : invalidAttributesException.getMessage().split(",")){
                map.put(error, true);
            }
            model.addAttribute("errors", map);
            return "auth/register";
        }catch (InternalError e){
            System.out.println("gg2");
            return "redirect:/register?error="+e.getMessage();
        }

        System.out.println("gg3");
        return "redirect:/login";
    }
}
