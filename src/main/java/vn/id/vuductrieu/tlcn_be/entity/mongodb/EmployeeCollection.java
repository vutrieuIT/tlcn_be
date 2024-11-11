package vn.id.vuductrieu.tlcn_be.entity.mongodb;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "employee")
public class EmployeeCollection {

    @Id
    private String id;

    private String name;

    private String email;

    private String phone;

    private String address;

    private String account;

    private String password;

    private Integer role;
}
