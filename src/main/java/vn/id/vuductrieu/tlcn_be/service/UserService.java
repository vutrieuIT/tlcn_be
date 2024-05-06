package vn.id.vuductrieu.tlcn_be.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import vn.id.vuductrieu.tlcn_be.dto.LoginDto;
import vn.id.vuductrieu.tlcn_be.dto.RegisterDto;
import vn.id.vuductrieu.tlcn_be.entity.UserEntity;
import vn.id.vuductrieu.tlcn_be.repository.UserRepository;

import java.util.Base64;
import java.util.Objects;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService {

    private final UserRepository userRepository;

    private final EmailService emailService;

    public void register(RegisterDto registerDto) {
        String error = validateRegisterDto(registerDto);
        if (!error.isEmpty()) {
            throw new IllegalArgumentException(error);
        }
        userRepository.findByEmail(registerDto.email).ifPresent(
                userEntity -> {
                    throw new IllegalArgumentException("Email is already taken");
                }
        );
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(registerDto, userEntity);

        String pwd = BCrypt.hashpw(registerDto.getPassword(), BCrypt.gensalt());
        userEntity.setPassword(pwd);
        userRepository.save(userEntity);

    }

    public void login(LoginDto loginDto) {
        String error = validateLoginDto(loginDto);
        if (!error.isEmpty()) {
            throw new IllegalArgumentException(error);
        }
        UserEntity userEntity = userRepository.findByEmail(loginDto.email).orElseThrow(
                () -> new IllegalArgumentException("Email not found")
        );
        if (!BCrypt.checkpw(loginDto.password, userEntity.getPassword())) {
            throw new IllegalArgumentException("Password is incorrect");
        }
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

    public void changePasswordForgot(RegisterDto verifyUserDto) {
        UserEntity userEntity = userRepository.findByEmail(verifyUserDto.getEmail()).orElseThrow(
                () -> new IllegalArgumentException("Email not found")
        );
        if (!Objects.equals(userEntity.getReset_code(), verifyUserDto.getReset_code())) {
            throw new IllegalArgumentException("Code is incorrect");
        }

        if (Objects.equals(verifyUserDto.getPassword(), verifyUserDto.getPassword_confirmation())) {
            throw new IllegalArgumentException("Password and password confirmation must be the same");
        }

        String pwd = BCrypt.hashpw(verifyUserDto.getPassword(), BCrypt.gensalt());
        userEntity.setPassword(pwd);
        userRepository.save(userEntity);
    }

    public void changePassword(RegisterDto changePasswordDto) {
        UserEntity userEntity = userRepository.findByEmail(changePasswordDto.getEmail()).orElseThrow(
                () -> new IllegalArgumentException("Email not found")
        );
        String pwd = BCrypt.hashpw(changePasswordDto.getPassword(), BCrypt.gensalt());
        userEntity.setPassword(pwd);
        userRepository.save(userEntity);
    }

    private String validateRegisterDto(RegisterDto registerDto) {
        StringBuilder error = new StringBuilder();
        if (registerDto.name == null || registerDto.name.isEmpty()) {
            error.append("Name is required");
        }
        if (registerDto.email == null || registerDto.email.isEmpty()) {
            error.append("Email is required");
        }
        if (registerDto.password == null || registerDto.password.isEmpty()) {
            error.append("Password is required");
        }
        if (registerDto.password_confirmation == null || registerDto.password_confirmation.isEmpty()) {
            error.append("Password confirmation is required");
        }
        if (Objects.equals(registerDto.password, registerDto.password_confirmation)) {
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
}
