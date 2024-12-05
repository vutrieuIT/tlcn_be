package vn.id.vuductrieu.tlcn_be.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import vn.id.vuductrieu.tlcn_be.entity.mongodb.EmployeeCollection;
import vn.id.vuductrieu.tlcn_be.entity.mongodb.UserCollection;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class TokenUtils {

    @Value("${myapp.security.jwt.secret:bXlfc2VjcmV0X2tleQ==}")
    private String SECRET_KEY;

    @Value("${myapp.security.jwt.expiration:3600000}")
    private long EXPIRATION_TIME;

    public String generateMongoToken(UserCollection userCollection) {
        return Jwts.builder()
            .setSubject(userCollection.getEmail())
            .claim("id", userCollection.getId())
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .signWith(SignatureAlgorithm.HS512, getSigningKey())
            .compact();
    }

    public String generateMongoToken(EmployeeCollection employeeCollection) {
        return Jwts.builder()
            .setSubject(employeeCollection.getName())
            .claim("id", employeeCollection.getId())
            .claim("role", employeeCollection.getRole())
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .signWith(SignatureAlgorithm.HS512, getSigningKey())
            .compact();
    }

    // Lấy thông tin từ JWT token
    public Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(getSigningKey())
                .parseClaimsJws(token)
                .getBody();
    }

    // Lấy username từ JWT token
    public String getUsernameFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    // Xác thực JWT token
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(getSigningKey()).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    private Key getSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(SECRET_KEY);
        return new SecretKeySpec(keyBytes, SignatureAlgorithm.HS512.getJcaName());
    }
}
