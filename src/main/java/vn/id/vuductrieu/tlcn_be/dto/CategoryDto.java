package vn.id.vuductrieu.tlcn_be.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import vn.id.vuductrieu.tlcn_be.entity.ProductEntity;

import java.io.Serializable;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CategoryDto implements Serializable {

    private Integer id;

    private String name;

    private String slug;

    private Integer position;

    private Boolean showHide;

    private List<ProductEntity> products;
}
