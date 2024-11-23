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
            () -> new IllegalArgumentException("Không tìm thấy email")
        );

        if (!BCrypt.checkpw(loginDto.password, userCollection.getPassword())) {
            throw new IllegalArgumentException("Mật khẩu không đúng");
        }
        if (Objects.equals(userCollection.getStatus(), Constants.Status.INACTIVE.getValue())) {
            throw new IllegalArgumentException("Tài khoản đã bị khóa");
        }

        return userCollection;
    }

    public UserCollection loginGoogle(String clientToken) {
        if (!verifyTokenGoogle(clientToken)) {
            throw new IllegalArgumentException("Token Google không hợp lệ");
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
                throw new IllegalArgumentException("Email đã tồn tại");
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
            error.append("Email không được để trống");
        }
        if (loginDto.password == null || loginDto.password.isEmpty()) {
            error.append("Mật khẩu không được để trống");
        }
        return String.join(", ", error);
    }

    private String validateUserDto(UserDto UserDto) {
        StringBuilder error = new StringBuilder();
        if (UserDto.name == null || UserDto.name.isEmpty()) {
            error.append("Tên không được để trống");
        }
        if (UserDto.email == null || UserDto.email.isEmpty()) {
            error.append("Email không được để trống");
        }
        if (UserDto.password == null || UserDto.password.isEmpty()) {
            error.append("Mật khẩu không được để trống");
        }
        if (UserDto.password_confirmation == null || UserDto.password_confirmation.isEmpty()) {
            error.append("Mật khẩu xác nhận không được để trống");
        }
        if (!Objects.equals(UserDto.password, UserDto.password_confirmation)) {
            error.append("Mật khẩu và mật khẩu xác nhận phải giống nhau");
        }
        return String.join(", ", error);
    }

    public void forgotPassword(String email) {
        UserCollection userCollection = userRepo.findByEmail(email).orElseThrow(
            () -> new IllegalArgumentException("Không tìm thấy email")
        );
        // random code 6 digits
        String code = String.valueOf((int) (Math.random() * 900000 + 100000));
        userCollection.setResetCode(code);
        userRepo.save(userCollection);
        emailService.sendEmail(email, "Quên mật khẩu", "Mã đặt lại mật khẩu của bạn: " + code);
    }

    public void changePasswordForgot(UserDto verifyCodeDto) {
        UserCollection userCollection = userRepo.findByEmail(verifyCodeDto.email).orElseThrow(
            () -> new IllegalArgumentException("không tìm thấy email")
        );
        if (!Objects.equals(userCollection.getResetCode(), verifyCodeDto.code)) {
            throw new IllegalArgumentException("Mã xác nhận không đúng");
        }
        if (Objects.equals(verifyCodeDto.getPassword(), verifyCodeDto.getPassword_confirmation())) {
            throw new IllegalArgumentException("Mật khẩu và mật khẩu xác nhận phải giống nhau");
        }
        userCollection.setPassword(BCrypt.hashpw(verifyCodeDto.password, BCrypt.gensalt()));
        userCollection.setResetCode(null);
        userRepo.save(userCollection);
    }

    public void changePassword(UserDto userDto) {
        UserCollection userCollection = userRepo.findById(userDto.user_id).orElseThrow(
            () -> new IllegalArgumentException("Không tìm thấy user")
        );
        if (!BCrypt.checkpw(userDto.password_old, userCollection.getPassword())) {
            throw new IllegalArgumentException("Mật khẩu cũ không đúng");
        }
        userCollection.setPassword(BCrypt.hashpw(userDto.password, BCrypt.gensalt()));
        userRepo.save(userCollection);
    }

    public List<UserCollection> getAllUser() {
        return userRepo.findAllExceptCartAndPassAndCode();
    }

    public void updateStatus(UserCollection userCollection) {
        UserCollection user = userRepo.findById(userCollection.getId()).orElseThrow(
            () -> new IllegalArgumentException("Không tìm thấy user")
        );
        user.setStatus(userCollection.getStatus());
        userRepo.save(user);
    }

    public UserCollection getUserInfo(String id) {
        return userRepo.findByIdExceptCartAndPassAndCode(id).orElseThrow(
            () -> new IllegalArgumentException("Không tìm thấy user")
        );
    }
}
