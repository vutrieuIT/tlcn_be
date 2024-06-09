package vn.id.vuductrieu.tlcn_be.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import vn.id.vuductrieu.tlcn_be.entity.ProductVariationEntity;

import java.io.Serializable;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductDto implements Serializable {

    private Integer id;

    private String name;

    private String seo_keywords;

    private String image_url;

    private Integer category_id;

    private Integer brand_id;

    private String description;

    private Integer show_hide;

    private CategoryDto category;

    private List<ProductVariationEntity> variations;
}
