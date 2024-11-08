package vn.id.vuductrieu.tlcn_be.service.MongoService;

import com.google.auth.oauth2.TokenVerifier;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import vn.id.vuductrieu.tlcn_be.constants.Constants;
import vn.id.vuductrieu.tlcn_be.dto.LoginDto;
import vn.id.vuductrieu.tlcn_be.dto.UserDto;
import vn.id.vuductrieu.tlcn_be.entity.UserEntity;
import vn.id.vuductrieu.tlcn_be.entity.mongodb.UserCollection;
import vn.id.vuductrieu.tlcn_be.repository.mongodb.UserRepo;
import vn.id.vuductrieu.tlcn_be.service.EmailService;

import java.util.Base64;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserMongoService {

    private final UserRepo userRepo;
    private final EmailService emailService;

    public UserCollection login(LoginDto loginDto) {
        String error = validateLoginDto(loginDto);
        if (!error.isEmpty()) {
            throw new IllegalArgumentException(error);
        }
        UserCollection userCollection = userRepo.findByEmail(loginDto.email).orElseThrow(
            () -> new IllegalArgumentException("Email not found")
        );

        if (!BCrypt.checkpw(loginDto.password, userCollection.getPassword())) {
            throw new IllegalArgumentException("Password is incorrect");
        }
        if (Objects.equals(userCollection.getStatus(), Constants.Status.INACTIVE.getValue())) {
            throw new IllegalArgumentException("account in inactive");
        }

        return userCollection;
    }

    public UserCollection loginGoogle(String clientToken) {
        if (!verifyTokenGoogle(clientToken)) {
            throw new IllegalArgumentException("Token is invalid");
        }
        String[] split = clientToken.split("\\.");
        String payload = new String(Base64.getDecoder().decode(split[1]));
        String email = payload.split("\"email\":\"")[1].split("\"")[0];
        UserCollection userCollection = userRepo.findByEmail(email).orElseGet(() -> {
            UserCollection user = new UserCollection();
            user.setEmail(email);
            user.setName(payload.split("\"name\":\"")[1].split("\"")[0]);
            user.setStatus(Constants.Status.ACTIVE.getValue());
            user.setPassword("");
            return userRepo.save(user);
        });
        return userCollection;
    }

    public UserCollection register(UserDto userDto) {
        String error = validateUserDto(userDto);
        if (!error.isEmpty()) {
            throw new IllegalArgumentException(error);
        }

        userRepo.findByEmail(userDto.email).ifPresent(
            userCollection -> {
                throw new IllegalArgumentException("Email is already taken");
            }
        );

        UserCollection userCollection = new UserCollection();
        userCollection.setEmail(userDto.email);
        userCollection.setName(userDto.name);
        userCollection.setPassword(BCrypt.hashpw(userDto.password, BCrypt.gensalt()));
        userCollection.setStatus(Constants.Status.ACTIVE.getValue());
        return userRepo.save(userCollection);
    }

    public boolean verifyTokenGoogle(String clientToken) {
        try {
            TokenVerifier verifier = TokenVerifier.newBuilder()
                .setAudience(Dotenv.load().get("GOOGLE_CLIENT_ID"))
                .build();
            verifier.verify(clientToken);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private String validateLoginDto(LoginDto loginDto) {
        StringBuilder error = new StringBuilder();
        if (loginDto.email == null || loginDto.email.isEmpty()) {
            error.append("Email is required");
        }
        if (loginDto.password == null || loginDto.password.isEmpty()) {
            error.append("Password is required");
        }
        return String.join(", ", error);
    }

    private String validateUserDto(UserDto UserDto) {
        StringBuilder error = new StringBuilder();
        if (UserDto.name == null || UserDto.name.isEmpty()) {
            error.append("Name is required");
        }
        if (UserDto.email == null || UserDto.email.isEmpty()) {
            error.append("Email is required");
        }
        if (UserDto.password == null || UserDto.password.isEmpty()) {
            error.append("Password is required");
        }
        if (UserDto.password_confirmation == null || UserDto.password_confirmation.isEmpty()) {
            error.append("Password confirmation is required");
        }
        if (!Objects.equals(UserDto.password, UserDto.password_confirmation)) {
            error.append("Password and password confirmation must be the same");
        }
        return String.join(", ", error);
    }

    public void forgotPassword(String email) {
        UserCollection userCollection = userRepo.findByEmail(email).orElseThrow(
            () -> new IllegalArgumentException("Email not found")
        );
        // random code 6 digits
        String code = String.valueOf((int) (Math.random() * 900000 + 100000));
        userCollection.setResetCode(code);
        userRepo.save(userCollection);
        emailService.sendEmail(email, "Forgot password", "Your code is: " + code);
    }

    public void changePasswordForgot(UserDto verifyCodeDto) {
        UserCollection userCollection = userRepo.findByEmail(verifyCodeDto.email).orElseThrow(
            () -> new IllegalArgumentException("Email not found")
        );
        if (!Objects.equals(userCollection.getResetCode(), verifyCodeDto.code)) {
            throw new IllegalArgumentException("Code is incorrect");
        }
        if (Objects.equals(verifyCodeDto.getPassword(), verifyCodeDto.getPassword_confirmation())) {
            throw new IllegalArgumentException("Password and password confirmation must be the same");
        }
        userCollection.setPassword(BCrypt.hashpw(verifyCodeDto.password, BCrypt.gensalt()));
        userCollection.setResetCode(null);
        userRepo.save(userCollection);
    }

    public void changePassword(UserDto userDto) {
        UserCollection userCollection = userRepo.findById(userDto.user_id).orElseThrow(
            () -> new IllegalArgumentException("user not found")
        );
        if (!BCrypt.checkpw(userDto.password_old, userCollection.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }
        userCollection.setPassword(BCrypt.hashpw(userDto.password, BCrypt.gensalt()));
        userRepo.save(userCollection);
    }

    public List<UserCollection> getAllUser() {
        return userRepo.findAllExceptCartAndPassAndCode();
    }

    public void updateStatus(UserCollection userCollection) {
        UserCollection user = userRepo.findById(userCollection.getId()).orElseThrow(
            () -> new IllegalArgumentException("user not found")
        );
        user.setStatus(userCollection.getStatus());
        userRepo.save(user);
    }
}
