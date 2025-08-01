package gift.util;

import gift.entity.Member;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Optional;

@Component
public class JwtTokenProvider {

    private final SecretKey key;
    private final long expirationTime;

    public JwtTokenProvider(@Value("${jwt.secret.key}") String secretKey,
                            @Value("${jwt.expiration.ms}") long expirationTime) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
        this.expirationTime = expirationTime;
    }

    public String getAccessToken(Member member) {

        Date now = new Date(System.currentTimeMillis());
        Date expiry = new Date(System.currentTimeMillis() + expirationTime);

        return Jwts.builder()
                .subject(member.getId().toString())
                .claim("email", member.getEmail())
                .claim("role", member.getRole().name())
                .issuedAt(now)
                .expiration(expiry)
                .signWith(key)
                .compact();
    }

    public Optional<String> resolveAccessToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring((7)).describeConstable();
        }

        return Optional.empty();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getIdFromToken(String token) {

        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}
