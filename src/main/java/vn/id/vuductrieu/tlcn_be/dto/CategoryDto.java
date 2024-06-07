package vn.id.vuductrieu.tlcn_be.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import vn.id.vuductrieu.tlcn_be.entity.ProductEntity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CategoryDto implements Serializable {

    private Integer id;

    private String name;

    private String slug;

    private Integer position;

    private Boolean showHide;

    private LocalDateTime created_at;

    private LocalDateTime updated_at;

    private List<ProductEntity> products;
}
