package vn.id.vuductrieu.tlcn_be.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Data;
import vn.id.vuductrieu.tlcn_be.composite_key.RatingKey;

@Data
@Entity
@Table(name = "ratings")
@IdClass(RatingKey.class)
public class RatingEntity {

    @Id
    @Column(name = "user_id")
    private Integer userId;
    @Id
    @Column(name = "cellphone_id")
    private Integer cellphoneId;

    @Column(name = "rating")
    private Integer rating;

    @Column(name = "comment")
    private String comment;
}
