package antidimon.web.front.services;

import antidimon.web.front.models.dto.MyUserRegisterDTO;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public class AuthService {


    public void save(@Valid MyUserRegisterDTO user) {
    }
}
