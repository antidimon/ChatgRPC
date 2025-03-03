package antidimon.web.front;

import antidimon.web.front.repositories.MyUserRepository;
import antidimon.web.front.security.JwtProvider;
import antidimon.web.front.security.filters.JwtRequestFilter;
import antidimon.web.front.security.handlers.AuthFailureHandler;
import antidimon.web.front.security.handlers.AuthSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebSecurity
public class Config {

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private MyUserRepository myUserRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(registry -> {
                    registry.requestMatchers(HttpMethod.POST, "/login", "/register").permitAll();
                    registry.requestMatchers(HttpMethod.GET,"/css/**", "/js/**","/images/*", "/login", "/register").permitAll();
                    registry.anyRequest().authenticated();})
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(httpSecurityFormLoginConfigurer -> httpSecurityFormLoginConfigurer
                        .loginPage("/login")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .successHandler(new AuthSuccessHandler(jwtProvider))
                        .permitAll()
                        .failureHandler(new AuthFailureHandler()))
                .logout((logout) -> {
                    logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout", "POST"));
                    logout.invalidateHttpSession(true);
                    logout.clearAuthentication(true);
                    logout.deleteCookies("JWT");
                    logout.deleteCookies("JSESSIONID");
                    logout.logoutSuccessUrl("/login");
                })
                .addFilterBefore(new JwtRequestFilter(jwtProvider, myUserRepository), UsernamePasswordAuthenticationFilter.class)

                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(5);
    }
}
