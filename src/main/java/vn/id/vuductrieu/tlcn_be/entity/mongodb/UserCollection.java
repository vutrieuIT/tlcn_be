package vn.id.vuductrieu.tlcn_be.entity.mongodb;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import vn.id.vuductrieu.tlcn_be.entity.mongodb.document.ItemDocument;

import java.util.List;

@Data
@Document(collection = "user")
public class UserCollection {

    @Id
    private String id;

    private String name;

    private String email;

    private String phone;

    private String address;

    private String password;

    private String resetCode;

    private List<ItemDocument> cart;

    private String status;
}
