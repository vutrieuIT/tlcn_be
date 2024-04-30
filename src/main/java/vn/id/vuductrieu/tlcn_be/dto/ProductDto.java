package vn.id.vuductrieu.tlcn_be.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import vn.id.vuductrieu.tlcn_be.entity.ProductVariationEntity;

import java.io.Serializable;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductDto implements Serializable {

    private Long id;

    private String name;

    private String seoKeywords;

    private String imageUrl;

    private Long brandId;

    private String description;

    private Boolean showHide;

    private List<ProductVariationEntity> variations;
}
