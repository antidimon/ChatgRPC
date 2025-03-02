package antidimon.web.front.security;

import antidimon.web.front.repositories.MyUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private MyUserRepository myUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<MyUser> foundedUser = myUserRepository.findByUsername(username);
        if (foundedUser.isEmpty()) {
            throw new UsernameNotFoundException(username);
        }
        return new MyUserDetails(foundedUser.get());
    }

}
