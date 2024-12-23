package vn.id.vuductrieu.tlcn_be.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.id.vuductrieu.tlcn_be.constants.MyConstants;
import vn.id.vuductrieu.tlcn_be.dto.LoginMongoDto;
import vn.id.vuductrieu.tlcn_be.dto.UserMongoDto;
import vn.id.vuductrieu.tlcn_be.entity.UserCollection;
import vn.id.vuductrieu.tlcn_be.service.UserMongoService;
import vn.id.vuductrieu.tlcn_be.service.PermissionService;
import vn.id.vuductrieu.tlcn_be.utils.TokenUtils;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class UserMongoController {

    private final UserMongoService userMongoService;

    private final PermissionService permissionService;

    private final TokenUtils tokenUtils;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody(required = false) LoginMongoDto loginMongoDto) {
        try {
            UserCollection userCollection = userMongoService.login(loginMongoDto);
            Map<String, Object> response = Map.of(
                "id", userCollection.getId(),
                "email", userCollection.getEmail(),
                "name", userCollection.getName(),
                "token", tokenUtils.generateMongoToken(userCollection));
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
            UserCollection userCollection = userMongoService.loginGoogle(clientToken);
            Map<String, Object> response = Map.of(
                "id", userCollection.getId(),
                "email", userCollection.getEmail(),
                "name", userCollection.getName(),
                "token", tokenUtils.generateMongoToken(userCollection)
            );
            return ResponseEntity.ok().body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/dang-ky")
    public ResponseEntity<?> register(@RequestBody(required = false) UserMongoDto UserDto) {
        try {
            UserCollection userCollection = userMongoService.register(UserDto);
            Map<String, Object> response = Map.of(
                "id", userCollection.getId(),
                "email", userCollection.getEmail(),
                "name", userCollection.getName(),
                "token", tokenUtils.generateMongoToken(userCollection)
            );
            return ResponseEntity.ok().body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody String email) {
        try {
            userMongoService.forgotPassword(email);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/verify-code")
    public ResponseEntity<?> changePasswordForgot(@RequestBody UserMongoDto verifyCodeDto) {
        try {
            userMongoService.changePasswordForgot(verifyCodeDto);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody UserMongoDto userDto) {
        try {
            userMongoService.changePassword(userDto);
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
            if (!permissionService.checkRole(MyConstants.Role.EMPLOYEE.getValue(), MyConstants.Role.ADMIN.getValue())) {
                return ResponseEntity.status(403).body("Bạn không có quyền");
            }
            List<UserCollection> userCollectionList = userMongoService.getAllUser();
            return ResponseEntity.ok().body(userCollectionList);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/user/status")
    public ResponseEntity<?> getAllUser(@RequestBody UserCollection userCollection) {
        try {
            if (!permissionService.checkRole(MyConstants.Role.EMPLOYEE.getValue(), MyConstants.Role.ADMIN.getValue())) {
                return ResponseEntity.status(403).body("Bạn không có quyền");
            }
            userMongoService.updateStatus(userCollection);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/user-info/{id}")
    public ResponseEntity<?> getUserInfo(@PathVariable String id) {
        try {
            if (!permissionService.checkRole(MyConstants.Role.EMPLOYEE.getValue(), MyConstants.Role.ADMIN.getValue())
                && !permissionService.getUserId().equals(id)) {
                return ResponseEntity.status(403).body("Bạn không có quyền");
            }
            UserCollection userCollection = userMongoService.getUserInfo(id);
            return ResponseEntity.ok().body(userCollection);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
