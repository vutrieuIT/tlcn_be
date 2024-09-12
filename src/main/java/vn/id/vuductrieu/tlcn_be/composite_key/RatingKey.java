package vn.id.vuductrieu.tlcn_be.composite_key;

import lombok.Data;

import java.io.Serializable;

@Data
public class RatingKey implements Serializable {
    private Integer userId;
    private Integer cellphoneId;
}
