package vn.id.vuductrieu.tlcn_be.dto.mongodb;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GHNItemDto {

    @JsonProperty("name")
    private String name;

    @JsonProperty("code")
    private String code;


    @JsonProperty("quantity")
    private Integer quantity;

    @JsonProperty("price")
    private Long price;

    @JsonProperty("weight")
    private Integer weight;

    @JsonProperty("length")
    private Integer length;

    @JsonProperty("width")
    private Integer width;

    @JsonProperty("height")
    private Integer height;

    @JsonProperty("category")
    private String category;

    @JsonProperty("level1")
    private String level1;

    @JsonProperty("cod_failed_amount")
    private Long codFailedAmount;
}
