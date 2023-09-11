package echo.echome.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Getter
public class JwtUtil {

    @Value("${jwt.secretKey}")
    String secretKey;

    @Value("${jwt.access.expiration}")
    Long accessExpiration;

    @Value("${jwt.refresh.expiration}")
    Long refreshExpiration;

    public boolean isExpired(String token){
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());
    }

    public String getEmail(String token){
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
                .getBody().get("email", String.class);
    }

    public Token createToken(String email){
        return Token.builder()
                .accessToken(createAccessToken(email))
                .refreshToken(createRefreshToken())
                .build();
    }

    private String createAccessToken(String email) {
        Claims claims = Jwts.claims();
        claims.put("email",email);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+accessExpiration))
                .signWith(SignatureAlgorithm.HS256,secretKey)
                .compact();
    }
    private String createRefreshToken() {
        return Jwts.builder()
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpiration))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }



}
