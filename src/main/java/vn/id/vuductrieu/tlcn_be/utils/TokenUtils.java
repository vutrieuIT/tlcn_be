package vn.id.vuductrieu.tlcn_be.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import vn.id.vuductrieu.tlcn_be.entity.UserEntity;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class TokenUtils {

    // Sử dụng giá trị mã hóa Base64 hợp lệ
    private final String SECRET_KEY = "bXlfc2VjcmV0X2tleQ==";  // Base64 encoded string
    private final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 hour in milliseconds

    // Tạo JWT token
    public String generateToken(UserEntity userEntity) {
        return Jwts.builder()
                .setSubject(userEntity.getEmail())
                .claim("role", userEntity.getRole())
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
