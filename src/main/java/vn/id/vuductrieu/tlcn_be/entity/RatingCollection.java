package vn.id.vuductrieu.tlcn_be.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "rating")
public class RatingCollection {

    private String id;

    private String userId;

    private String username;

    private String productId;

    private Integer rating;

    private String comment;
}
