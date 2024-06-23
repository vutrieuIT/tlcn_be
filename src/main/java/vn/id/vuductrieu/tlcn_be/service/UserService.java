package vn.id.vuductrieu.tlcn_be.service;

import com.google.auth.oauth2.TokenVerifier;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import vn.id.vuductrieu.tlcn_be.constants.Constants;
import vn.id.vuductrieu.tlcn_be.dto.LoginDto;
import vn.id.vuductrieu.tlcn_be.dto.UserDto;
import vn.id.vuductrieu.tlcn_be.entity.UserEntity;
import vn.id.vuductrieu.tlcn_be.repository.UserRepository;

import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService {

    private final UserRepository userRepository;

    private final EmailService emailService;

    public UserEntity register(UserDto UserDto) {
        String error = validateUserDto(UserDto);
        if (!error.isEmpty()) {
            throw new IllegalArgumentException(error);
        }
        userRepository.findByEmail(UserDto.email).ifPresent(
                userEntity -> {
                    throw new IllegalArgumentException("Email is already taken");
                }
        );
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(UserDto, userEntity);

        String pwd = BCrypt.hashpw(UserDto.getPassword(), BCrypt.gensalt());
        userEntity.setPassword(pwd);
        userEntity.setRole(Constants.Role.USER.getValue());
        userEntity.setStatus(Constants.Status.ACTIVE.getValue());
        return userRepository.save(userEntity);
    }

    public UserEntity login(LoginDto loginDto) {
        String error = validateLoginDto(loginDto);
        if (!error.isEmpty()) {
            throw new IllegalArgumentException(error);
        }
        UserEntity userEntity = userRepository.findByEmail(loginDto.email).orElseThrow(
                () -> new IllegalArgumentException("Email not found")
        );
        if (userEntity.getStatus() == Constants.Status.INACTIVE.getValue()) {
            throw new IllegalArgumentException("account in inactive");
        }
        if (!BCrypt.checkpw(loginDto.password, userEntity.getPassword())) {
            throw new IllegalArgumentException("Password is incorrect");
        }
        return userEntity;
    }

    public void forgotPassword(String email) {
        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(
                () -> new IllegalArgumentException("Email not found")
        );
        // random code 6 digits
        String code = String.valueOf((int) (Math.random() * 900000 + 100000));
        userEntity.setReset_code(code);
        userRepository.save(userEntity);
        emailService.sendEmail(email, "Forgot password", "Your code is: " + code);
    }

    public void changePasswordForgot(UserDto verifyUserDto) {
        UserEntity userEntity = userRepository.findByEmail(verifyUserDto.getEmail()).orElseThrow(
                () -> new IllegalArgumentException("Email not found")
        );
        if (!Objects.equals(userEntity.getReset_code(), verifyUserDto.getCode())) {
            throw new IllegalArgumentException("Code is incorrect");
        }

        if (Objects.equals(verifyUserDto.getPassword(), verifyUserDto.getPassword_confirmation())) {
            throw new IllegalArgumentException("Password and password confirmation must be the same");
        }

        String pwd = BCrypt.hashpw(verifyUserDto.getPassword(), BCrypt.gensalt());
        userEntity.setPassword(pwd);
        userRepository.save(userEntity);
    }

    public void changePassword(UserDto changePasswordDto) {
        UserEntity userEntity = userRepository.findById(Integer.parseInt(changePasswordDto.getUser_id())).orElseThrow(
                () -> new IllegalArgumentException("user not found")
        );
        if (!BCrypt.checkpw(changePasswordDto.getPassword_old(), userEntity.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }
        String pwd = BCrypt.hashpw(changePasswordDto.getPassword(), BCrypt.gensalt());
        userEntity.setPassword(pwd);
        userRepository.save(userEntity);
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

    public List<UserEntity> getAllUser() {
        List<UserEntity> userEntities = userRepository.findAll();
        return userEntities;
    }

    public void updateStatus(UserEntity userEntity) {
        UserEntity user = userRepository.findByEmail(userEntity.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("email not found"));
        user.setStatus(userEntity.getStatus());
        userRepository.save(user);
    }

    public UserEntity loginGoogle(String clientToken) {
        if (!verifyTokenGoogle(clientToken)) {
            throw new IllegalArgumentException("Token is invalid");
        }
        String[] split = clientToken.split("\\.");
        String payload = new String(Base64.getDecoder().decode(split[1]));
        String email = payload.split("\"email\":\"")[1].split("\"")[0];
        UserEntity userEntity = userRepository.findByEmail(email).orElseGet(() -> {
            UserEntity user = new UserEntity();
            user.setEmail(email);
            user.setName(payload.split("\"name\":\"")[1].split("\"")[0]);
            user.setRole(Constants.Role.USER.getValue());
            user.setStatus(Constants.Status.ACTIVE.getValue());
            return userRepository.save(user);
        });
        return userEntity;
    }

    public boolean verifyTokenGoogle(String clientToken){
        try {
            TokenVerifier verifier = TokenVerifier.newBuilder()
                    .setAudience(Dotenv.load().get("GOOGLE_CLIENT_ID"))
                    .build();
            verifier.verify(clientToken);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
