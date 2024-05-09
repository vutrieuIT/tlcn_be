package vn.id.vuductrieu.tlcn_be.dto;

import lombok.Data;

@Data
public class UserDto {

    public String name;
    public String email;
    public String password;
    public String password_confirmation;

    public String password_old;

    public String user_id;

    public String code;
}
