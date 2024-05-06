package vn.id.vuductrieu.tlcn_be.dto;

import lombok.Data;

@Data
public class RegisterDto {

    public String name;
    public String email;
    public String password;
    public String password_confirmation;

    public String reset_code;
}
