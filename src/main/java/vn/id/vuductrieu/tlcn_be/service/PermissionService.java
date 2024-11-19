package vn.id.vuductrieu.tlcn_be.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.id.vuductrieu.tlcn_be.constants.Constants;
import vn.id.vuductrieu.tlcn_be.utils.TokenUtils;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PermissionService {

    private final HttpServletRequest request;

    private final TokenUtils tokenUtils;

    public boolean isAdmin() {
        try {
            String token = request.getHeader("Authorization");
            if (token == null || !token.startsWith("Bearer ")) {
                return false;
            }

            if (!tokenUtils.validateToken(token.substring(7))) {
                return false;
            }

            Integer role = tokenUtils.getClaimsFromToken(token.substring(7)).get("role", Integer.class);
            return Objects.equals(role, Constants.Role.ADMIN.getValue());
        } catch (Exception e) {
            return false;
        }
    }

    public String getUserId() {
        try {
            String token = request.getHeader("Authorization");
            if (token == null || !token.startsWith("Bearer ")) {
                return null;
            }

            if (!tokenUtils.validateToken(token.substring(7))) {
                return null;
            }

            return tokenUtils.getClaimsFromToken(token.substring(7)).get("id", String.class);
        } catch (Exception e) {
            return null;
        }
    }
}
