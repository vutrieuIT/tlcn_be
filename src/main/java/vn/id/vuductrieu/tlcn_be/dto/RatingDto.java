package vn.id.vuductrieu.tlcn_be.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class RatingDto implements Serializable {

        private Integer userId;

        private Integer cellphoneId;

        private Integer rating;

        private String comment;
}
