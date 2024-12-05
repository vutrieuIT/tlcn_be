package vn.id.vuductrieu.tlcn_be.dto.mongodb;

import lombok.Data;

@Data
public class UserMongoDto {

    public String name;
    public String email;
    public String password;
    public String password_confirmation;

    public String password_old;

    public String user_id;

    public String code;
}
