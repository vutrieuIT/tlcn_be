package vn.id.vuductrieu.tlcn_be.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.id.vuductrieu.tlcn_be.dto.LoginDto;
import vn.id.vuductrieu.tlcn_be.dto.UserDto;
import vn.id.vuductrieu.tlcn_be.entity.UserEntity;
import vn.id.vuductrieu.tlcn_be.service.PermissionService;
import vn.id.vuductrieu.tlcn_be.service.UserService;
import vn.id.vuductrieu.tlcn_be.utils.TokenUtils;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {

    private final UserService userService;

    private final PermissionService permissionService;

    private final TokenUtils tokenUtils;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody(required = false) LoginDto loginDto) {
        try {
            UserEntity userEntity = userService.login(loginDto);
            Map<String, Object> response = Map.of(
                    "id", userEntity.getId(),
                    "email", userEntity.getEmail(),
                    "name", userEntity.getName(),
                    "token", tokenUtils.generateToken(userEntity)
            );
            return ResponseEntity.ok().body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/login-google")
    public ResponseEntity<?> loginGoogle(@RequestBody(required = false) String clientToken) {
        try {
            UserEntity userEntity = userService.loginGoogle(clientToken);
            Map<String, Object> response = Map.of(
                    "id", userEntity.getId(),
                    "email", userEntity.getEmail(),
                    "name", userEntity.getName(),
                    "token", tokenUtils.generateToken(userEntity)
            );
            return ResponseEntity.ok().body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/dang-ky")
    public ResponseEntity<?> register(@RequestBody(required = false) UserDto UserDto) {
        try {
            UserEntity userEntity = userService.register(UserDto);
            Map<String, Object> response = Map.of(
                    "id", userEntity.getId(),
                    "email", userEntity.getEmail(),
                    "name", userEntity.getName(),
                    "token", tokenUtils.generateToken(userEntity)
            );
            return ResponseEntity.ok().body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody UserEntity userEntity) {
        try {
            userService.forgotPassword(userEntity.getEmail());
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/verify-code")
    public ResponseEntity<?> changePasswordForgot(@RequestBody UserDto verifyCodeDto) {
        try {
            userService.changePasswordForgot(verifyCodeDto);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody UserDto changePasswordDto) {
        try {
            userService.changePassword(changePasswordDto);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/all-user")
    public ResponseEntity<?> getAllUser() {
        try {
            if (!permissionService.isAdmin()){
                return ResponseEntity.status(403).body("you need admin role");
            }
            List<UserEntity> userEntities = userService.getAllUser();
            return ResponseEntity.ok().body(userEntities);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/user/status")
    public ResponseEntity<?> getAllUser(@RequestBody UserEntity userEntity) {
        try {
            if (!permissionService.isAdmin()){
                return ResponseEntity.status(403).body("you need admin role");
            }
            userService.updateStatus(userEntity);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

}
