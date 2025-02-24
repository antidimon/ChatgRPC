package antidimon.web.userservice.services;


import antidimon.web.userservice.models.ChatUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class JwtService {

    @Value("${jwt}")
    private String token;
    private static final long VALIDITY = TimeUnit.HOURS.toMillis(1);

    public String generateToken(ChatUser chatUser) {
        return Jwts.builder()
                .subject(chatUser.getUsername())
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusMillis(VALIDITY)))
                .signWith(getSecretKey())
                .compact();
    }

    private SecretKey getSecretKey() {
        byte[] decodedKey = Base64.getDecoder().decode(token);
        return Keys.hmacShaKeyFor(decodedKey);
    }

    public boolean isValid(String jwt){
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(jwt).getPayload();
            return !claims.getExpiration().before(Date.from(Instant.now()));

        } catch (ExpiredJwtException e) {
            return false;
        }
    }
}
